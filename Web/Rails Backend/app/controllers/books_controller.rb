class BooksController < ApplicationController
  before_action :authenticate

  def index
    @books = search_book_query(params[:substring] || "", @token)
    render "index"
  end

  def followed
    if is_admin?(@token)
      render "layouts/error"
    else
      @books = followed_book_query(@token)
      render "index"
    end
  end

  def create
    if is_admin?(@token)
      if add_book_query(params[:isbn], params[:title], params[:author])
        render "layouts/successful"
      else
        render "layouts/error"
      end
    else
      render "layouts/error"
    end
  end

  def reserve
    user_id = get_user_id(@token)
    if can_reserve?(user_id, params[:isbn]) && ( copy_isn = first_available_copy(params[:isbn])) && reserve_book(user_id, copy_isn)
      render "layouts/successful"
    else
      render "layouts/error"
    end
  end

  def add_copy
    if is_admin?(@token) && add_copy_query(params[:isbn], params[:isn])
      render "layouts/successful"
    else
      render "layouts/error"
    end
  end

  def follow
    if !is_admin?(@token) && follow_book(@token, params[:isbn])
      render "layouts/successful"
    else
      render "layouts/error"
    end
  end

  def add_image
    if is_admin?(@token) && add_file_to_book(params[:isbn], params[:file])
      render "layouts/successful"
    else
      render "layouts/error"
    end
  end

  def upvote
    if is_professor?(@token) && upvote_book(@token, params[:isbn])
      render "layouts/successful"
    else
      render "layouts/error"
    end
  end
  private

  def can_reserve?(user_id, isbn)
    return false if is_admin_id?(user_id)
    reservations = active_reservations(user_id)
    return false if reservations.include?(isbn.to_i)
    return true if is_professor_id?(user_id)
    return reservations.count < student_book_limit(user_id)
  end
end
