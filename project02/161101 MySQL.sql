-- 게시판
ALTER TABLE boards
	DROP PRIMARY KEY; -- 게시판 기본키

-- 댓글
ALTER TABLE replys
	DROP PRIMARY KEY; -- 댓글 기본키

-- 게시판
CREATE TABLE boards (
	bno     INTEGER      NOT NULL, -- 게시물번호
	title   VARCHAR(255) NOT NULL, -- 제목
	content VARCHAR(255) NOT NULL, -- 내용
	user    VARCHAR(50)  NOT NULL, -- 작성자
	cre_dt  DATETIME     NOT NULL,  -- 등록일
    pwd		VARCHAR(30)  NOT NULL
);

-- 게시판
ALTER TABLE boards
	ADD CONSTRAINT PK_boards -- 게시판 기본키
		PRIMARY KEY (
			bno -- 게시물번호
		);

ALTER TABLE boards
	MODIFY COLUMN bno INTEGER NOT NULL AUTO_INCREMENT;

-- 댓글
CREATE TABLE replys (
	rno      INTEGER      NOT NULL, -- 댓글번호
	bno      INTEGER      NOT NULL, -- 게시물번호
	ruser    VARCHAR(50)  NOT NULL, -- 작성자
	rcontent VARCHAR(255) NOT NULL, -- 내용
	rcre_dt  DATETIME     NOT NULL  -- 등록일
);

-- 댓글
ALTER TABLE replys
	ADD CONSTRAINT PK_replys -- 댓글 기본키
		PRIMARY KEY (
			rno -- 댓글번호
		);

ALTER TABLE replys
	MODIFY COLUMN rno INTEGER NOT NULL AUTO_INCREMENT;

-- 댓글
ALTER TABLE replys
	ADD CONSTRAINT FK_boards_TO_replys -- 게시판 -> 댓글
		FOREIGN KEY (
			bno -- 게시물번호
		)
		REFERENCES boards ( -- 게시판
			bno -- 게시물번호
		);
		
--게시물 리스트
   	SELECT b1.bno, b1.title, b1.cre_dt, b1.user_name, r1.rp, b2.totalcount
    FROM boards b1
    LEFT OUTER JOIN (
		SELECT bno,COUNT(rno) rp 
		FROM replys
		GROUP BY bno) r1 
        ON b1.bno = r1.bno
	LEFT OUTER JOIN (
		SELECT COUNT(bno) totalcount FROM boards) b2
        ON b1.bno
    ORDER BY bno desc
    LIMIT #{startIndex}, #{len}

--게시물 상세보기  
    SELECT bno, title, content, user_name, cre_dt, pwd
    FROM boards
    WHERE bno=#{value} 
    
--비밀번호 체크    
		SELECT *
		FROM boards
		WHERE bno = #{bno}
		    
--게시물 쓰기
    INSERT INTO boards(title, content, user_name, cre_dt, pwd)
    VALUES(#{title}, #{content}, #{user}, now(), #{pwd})
  
--게시물 삭제
    DELETE FROM boards 
    WHERE bno=#{no}
    
--게시물 업데이트
    UPDATE boards SET
      title=#{title},
      content=#{content},
      user_name=#{user},
      cre_dt=now()
    WHERE bno=#{no}

--게시물 검색 리스트    
    SELECT bno, title, user_name, cre_dt
    FROM boards
	    <if test="sValue eq 'sTitle'">
		    	WHERE title Like CONCAT('%',#{sWord},'%')
	    </if>
	    <if test="sValue eq 'sUser'">
		    	WHERE user_name Like CONCAT('%',#{sWord},'%')
	    </if>
	    <if test="sValue eq 'sDate'">
		    	WHERE cre_dt Like CONCAT('%',#{sWord},'%')
	    </if>
    ORDER BY bno desc
    LIMIT #{startIndex}, #{len}
  
--게시물 댓글 리스트
    SELECT b1.bno, r1.ruser_name, r1.rcontent, r1.rcre_dt, r1.rno 
	FROM replys r1, boards b1
	WHERE r1.bno = b1.bno
	AND r1.bno = #{no}
	ORDER BY r1.rno DESC
    LIMIT #{startIndex}, #{len}
  
--게시물 댓글 쓰기
    INSERT INTO replys(bno, rcontent, ruser_name, rcre_dt)
    VALUES(#{no}, #{rcontent}, #{ruser}, now())



