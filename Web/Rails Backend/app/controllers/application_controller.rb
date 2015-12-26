class ApplicationController < ActionController::Base
  # Prevent CSRF attacks by raising an exception.
  # For APIs, you may want to use :null_session instead.
  protect_from_forgery with: :null_session

  def reservation_length
    45.days.from_now
  end

  def authenticate
    unless request.headers["authentication"]
      render "layouts/error"
      return false
    else
      @token = request.headers["authentication"].split(" ").last
      return true
    end
  end

  def is_admin?(token)
    q = "SELECT is_admin FROM user WHERE token='#{token}'"
    res = exec_sql_query q
    return (res.count > 0 && res.first.first == 1)
  end

  def is_admin_id?(id)
    q = "SELECT is_admin FROM user WHERE id='#{id}'"
    res = exec_sql_query q
    return (res.count > 0 && res.first.first == 1)
  end

  def is_professor?(token)
    q = "SELECT profstud_flag FROM reader, user WHERE user.token='#{token}' AND user.id = reader.id"
    res = exec_sql_query q
    return (res.count > 0 && res.first.first == 1)
  end

  def is_professor_id?(id)
    q = "SELECT profstud_flag FROM reader WHERE id = '#{id}'"
    res = exec_sql_query q
    return (res.count > 0 && res.first.first == 1)
  end

  def student_book_limit(user_id)
    q = "SELECT books_limit FROM reader WHERE id = '#{user_id}'"
    exec_sql_query(q).first.first
  end

  def get_user_id(token)
    q = "SELECT id FROM user WHERE token='#{token}'"
    res = exec_sql_query q
    res.first.first
  end

  def exec_sql_query(query)
    @connection ||= ActiveRecord::Base.connection
    begin
      return @connection.execute query
    rescue => error
      #TODO REMOVE PRINTS.
      Rails.logger.info $!.message
      Rails.logger.info error.backtrace
      return "ERROR"
    end
  end

  def time_formatter t
    t.to_s[0..18]
  end

  def date_formatter t
    t.to_s[0..9]
  end

  def form_hash fields, entry
    res = Hash.new
    fields.zip(entry).each do |k,v|
      res[k] = v
    end
    res
  end

  def set_user_type(q_res)
    res = form_hash(q_res.fields, q_res.first)
    if res["is_admin"] == 1
      res["type"] = "admin"
    else
      res["type"] = (res["profstud_flag"] == 1) ? "professor" : "student"
    end
    res.delete "is_admin"
    res.delete "profstud_flag"
    res
  end

  def user_object(id)
    q = "SELECT user.id,name,mail,is_admin,image_url,profstud_flag FROM user LEFT JOIN reader ON reader.id = user.id WHERE user.id = '#{id}'"
    q_res = exec_sql_query q
    res = set_user_type(q_res)
    res
  end

  def sign_up_query(name, hashed_password, email, is_admin, token, image_url, is_professor, code, type, book_limit)
    q = "INSERT INTO user (`id`, `name`, `hashed_password`, `mail`, `is_admin`, `token`, `image_url`) VALUES (NULL, '#{name}', '#{hashed_password}', '#{email}', '#{is_admin}','#{token}', '#{image_url}');"
    if exec_sql_query(q) != "ERROR"
      res = {
        name: name,
        mail: email,
        type: type,
        token: token
      }
      q = "SELECT LAST_INSERT_ID();"
      res[:id] = exec_sql_query(q).first.first
      if is_admin == 1
        q = "INSERT INTO admin (`id`, `joindate`) VALUES ('#{res[:id]}', '#{time_formatter(Time.now)}');"
      else
        q = "INSERT INTO reader (`id`, `code`, `profstud_flag`, `books_limit`) VALUES ('#{res[:id]}', '#{code}', '#{is_professor}', '#{(is_professor == 0)? book_limit : 0}')"
      end
      exec_sql_query q
      return res
    else
      return false
    end
  end

  def login_query(email, password, token)
    q = "SELECT user.id,name,mail,is_admin,image_url,profstud_flag FROM user LEFT JOIN reader ON reader.id = user.id WHERE mail='#{email}' AND hashed_password ='#{password}'"
    res = exec_sql_query(q)
    if res.count == 0
      return nil
    else
      res = set_user_type(res)
      res["token"] = token
      q = "UPDATE user SET token='#{res["token"]}' WHERE id='#{res["id"]}'"
      exec_sql_query q
      return res
    end
  end

  def add_book_query(isbn, title, author)
    q = "INSERT INTO book (`ISBN`, `title`, `author`, `img_url`) VALUES ('#{isbn}', '#{title}', '#{author}', NULL);"
    if exec_sql_query(q) != "ERROR"
      return true
    else
      return false
    end
  end

  def add_copy_query(isbn, isn)
    q = "INSERT INTO book_copy (`book_isbn`, `isn`, `is_available`) VALUES ('#{isbn}', '#{isn}', '1')"
    if exec_sql_query(q) != "ERROR"
      return true
    else
      return false
    end
  end

  def book_object(isbn, user_id)
    q = "SELECT isbn, title, author, img_url, COUNT(*) AS nUpvotes FROM book, votes WHERE isbn = #{isbn} AND book_isbn = isbn"
    q_res = exec_sql_query q
    res = form_hash(q_res.fields, q_res.entries.first)
    q = "SELECT 1 FROM book_copy WHERE book_isbn = '#{isbn}' AND is_available = '1'"
    q_res = exec_sql_query q
    if q_res.count > 0
      res["available"] = 1
    else
      res["available"] = 0
    end
    q = "SELECT 1 FROM votes WHERE professor_id = '#{user_id}' AND book_isbn = '#{isbn}'"
    q_res = exec_sql_query q
    if q_res.count > 0
      res["votedByMe"] = 1
    else
      res["votedByMe"] = 0
    end
    res
  end

  def search_book_query(substring, token)
    q = "SELECT b.title, b.isbn, b.author, b.img_url, COUNT(v.professor_id) AS nUpvotes, COUNT(c.isn) AND 1 AS available, COUNT(case v.professor_id when (SELECT id FROM user WHERE token = '#{token}') then 1 else null end) AS votedByMe FROM book b LEFT JOIN votes v ON b.isbn = v.book_isbn LEFT JOIN book_copy c ON b.isbn = c.book_isbn AND c.is_available = 1 WHERE ((b.title like '%#{substring}%') or (b.author like '%#{substring}%')) GROUP BY b.isbn"
    q_res = exec_sql_query(q)
    res = Array.new
    q_res.entries.each do |entry|
      res << form_hash(q_res.fields, entry)
    end
    res
  end

  def followed_book_query(token)
    user_id = get_user_id(token)
    q = "SELECT b.title, b.isbn, b.author, b.img_url, COUNT(v.professor_id) AS nUpvotes, COUNT(c.isn) AND 1 AS available, COUNT(case v.professor_id when (#{user_id}) then 1 else null end) AS votedByMe FROM follows f, book b LEFT JOIN votes v ON b.isbn = v.book_isbn LEFT JOIN book_copy c ON b.isbn = c.book_isbn AND c.is_available = 1 WHERE b.isbn = f.book_isbn AND f.reader_id = '#{user_id}' GROUP BY f.book_isbn"
    q_res = exec_sql_query(q)
    res = Array.new
    q_res.entries.each do |entry|
      res << form_hash(q_res.fields, entry)
    end
    res
  end

  def active_reservations(user_id)
    q = "SELECT book_isbn FROM book_copy, reservation WHERE reader_id = '#{user_id}' AND isn = copy_isn AND return_date IS NULL"
    exec_sql_query(q).entries.flatten
  end

  def first_available_copy(isbn)
    q = "SELECT isn FROM book_copy WHERE book_isbn = '#{isbn}' AND is_available = '1' LIMIT 1"
    res = exec_sql_query(q)
    puts res.count
    return false if res.count == 0
    res.first.first
  end

  def reserve_book(user_id, copy_isn)
    q = "INSERT INTO reservation (`reader_id`, `copy_isn`, `reservation_code`, `reservation_date`, `lending_date`, `return_date`, `return_deadline`, `return_admin_id`, `lending_admin_id`)
        VALUES ('#{user_id}' , '#{copy_isn}', NULL, '#{time_formatter(Time.now)}', NULL, NULL, '#{time_formatter(reservation_length)}', NULL, NULL)"
    if exec_sql_query(q) != "ERROR"
      q = "UPDATE book_copy SET is_available = '0' WHERE isn = '#{copy_isn}'"
      exec_sql_query q
      return true
    else
      return false
    end
  end

  def get_all_reservations(token)
    user_id = get_user_id(token)
    q = "SELECT reservation_code, reservation_date, lending_date, return_deadline, return_date, book_isbn, user.id FROM reservation, book_copy, user WHERE copy_isn = isn AND user.id = reader_id"
    q+= " AND reader_id = '#{user_id}'" unless is_admin_id?(user_id)
    q+= " ORDER BY reservation_date"
    res = Array.new
    q_res = exec_sql_query q
    q_res.entries.each_with_index do |entry, i|
      res << form_hash(q_res.fields, entry)
      uid = res[i].delete("id")
      res[i]["user"] = user_object(uid)
      isbn = res[i].delete("book_isbn")
      res[i]["book"] = book_object(isbn, user_id)
    end
    res
  end

  def follow_book(token, isbn)
    q = "INSERT INTO follows (`reader_id`, `book_isbn`) VALUES ((select id from user where token='#{token}'), '#{isbn}')"
    !(exec_sql_query(q) == "ERROR")
  end

  def upvote_book(token, isbn)
    q = "INSERT INTO votes (`professor_id`, `book_isbn`) VALUES ((select id from user where token='#{token}'), '#{isbn}')"
    !(exec_sql_query(q) == "ERROR")
  end

  def lend_book(token, reservation_code)
    q = "SELECT 1 FROM reservation WHERE `reservation_code` = #{reservation_code} AND lending_date IS NULL AND return_deadline > #{date_formatter(Time.now)}"
    return false if exec_sql_query(q).entries.count < 1
    q = "UPDATE reservation SET `lending_date` = '#{time_formatter(Time.now)}', `lending_admin_id` = (select id from user where token = '#{token}' ) WHERE `reservation`.`reservation_code` = #{reservation_code}"
    !(exec_sql_query(q) == "ERROR")
  end

  def return_book(token, reservation_code)
    q = "SELECT copy_isn FROM reservation WHERE `reservation_code` = #{reservation_code} AND lending_date IS NOT NULL"
    entries = exec_sql_query(q)
    return false if entries.count < 1
    copy_isn = entries.first.first
    q = "UPDATE reservation SET `return_date` = '#{time_formatter(Time.now)}', `return_admin_id` = (select id from user where token = '#{token}' ) WHERE `reservation`.`reservation_code` = #{reservation_code}"
    return false if (exec_sql_query(q) == "ERROR")
    q = "UPDATE book_copy SET is_available = '1' WHERE isn = '#{copy_isn}'"
    !(exec_sql_query(q) == "ERROR")
  end

  def add_file_to_book(isbn, file)
    directory = "public"
    name = SecureRandom.uuid + ".jpg"
    path = File.join(directory, name)
    File.open(path, "wb") { |f| f.write(file.read) }
    q = "UPDATE book SET img_url='http://library-themonster.rhcloud.com/#{name}' WHERE isbn='#{isbn}'"
    !(exec_sql_query(q) == "ERROR")
  end
end
