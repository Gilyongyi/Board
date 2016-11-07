DROP TABLE replys;
DROP TABLE boards;
DROP SEQUENCE seq_boards_bno;
DROP SEQUENCE seq_replys_rno;
CREATE TABLE boards (
  bno INTEGER NOT NULL,
  title VARCHAR2(255) NOT NULL,
  content VARCHAR2(255) NOT NULL,
  user_name VARCHAR2(50) NOT NULL,
  cre_dt DATE NOT NULL,
  pwd VARCHAR2(30) NOT NULL,
  file_name VARCHAR2(255),
  PRIMARY KEY(bno)
);

CREATE TABLE replys (
  rno INTEGER NOT NULL,
  bno INTEGER NOT NULL REFERENCES boards(bno),
  ruser_name VARCHAR2(50) NOT NULL,
  rcontent VARCHAR2(255) NOT NULL,
  rcre_dt DATE,
  PRIMARY KEY(rno)
)

CREATE SEQUENCE seq_boards_bno	 	  INCREMENT BY 1 START WITH 10000;
CREATE SEQUENCE seq_replys_rno	 	  INCREMENT BY 1 START WITH 10000;

SELECT * FROM boards
SELECT * FROM replys

--게시물 리스트
SELECT * FROM (
		SELECT ROWNUM rowno,v.bno,v.title,v.user_name,v.cre_dt,NVL(v.rp,'0') rp, v.totalcount
	  	FROM
			(SELECT b1.bno bno, b1.title title, b1.user_name user_name, b1.cre_dt cre_dt, b2.totalcount totalcount, NVL(r.rp,'0') rp
	    		FROM boards b1,
	        (SELECT bno,COUNT(rno) rp FROM replys GROUP BY bno) r,
	        (SELECT COUNT(bno) totalcount FROM boards) b2
	        WHERE b1.bno = r.bno(+)
	        ORDER BY bno DESC) v
	     ) vt
    WHERE vt.rowno between #{startIndex} and #{len}

--게시물 지우기
    DELETE FROM boards 
    WHERE bno=#{no}
  
--비밀번호 체크
    	SELECT *
		FROM boards
		WHERE bno = #{bno}
  
--게시물 상세보기
	SELECT bno, title, content, user_name, cre_dt, pwd
    FROM boards
    WHERE bno=#{value}     
--게시물 검색 리스트
    SELECT * FROM (
		SELECT ROWNUM rowno,v.bno,v.title,v.user_name,v.cre_dt,NVL(v.rp,'0') rp, v.totalcount
	  	FROM
			(SELECT b1.bno bno, b1.title title, b1.user_name user_name, b1.cre_dt cre_dt,
			 b2.totalcount totalcount, NVL(r.rp,'0') rp
	    	FROM boards b1,
	        (SELECT bno,COUNT(rno) rp FROM replys GROUP BY bno) r,
	        (SELECT COUNT(bno) totalcount FROM boards) b2
	        WHERE b1.bno = r.bno(+)
			    	<if test="sValue eq 'sTitle'">
			    	AND title Like '%'||#{sWord}||'%'
			    </if>
			    <if test="sValue eq 'sUser'">
				    AND user_name Like '%'||#{sWord}||'%'
			    </if>
			    <if test="sValue eq 'sDate'">
				    AND cre_dt Like '%'||#{sWord}||'%'
			    </if>
	        ORDER BY bno DESC) v
	     ) vt
    WHERE vt.rowno between #{startIndex} and #{len}
  
--댓글 리스트
    SELECT b1.bno, r1.ruser_name, r1.rcontent, r1.rcre_dt, r1.rno 
	FROM replys r1, boards b1
	WHERE r1.bno = b1.bno
	AND r1.bno = #{no}
	ORDER BY r1.rno DESC
  
--게시물 쓰기
	INSERT INTO boards(bno, title, content, user_name, cre_dt, pwd)
    VALUES(seq_boards_bno.nextval,#{title}, #{content}, #{user}, SYSDATE, #{pwd})
  
--댓글 쓰기
    INSERT INTO replys(rno, bno, rcontent, ruser_name, rcre_dt)
    VALUES(seq_boards_bno.nextval, #{no}, #{rcontent}, #{ruser}, SYSDATE)
  
--게시물 업데이트
    UPDATE boards SET
      title=#{title},
      content=#{content},
      user_name=#{user},
      cre_dt=SYSDATE
    WHERE bno=#{no}
  
