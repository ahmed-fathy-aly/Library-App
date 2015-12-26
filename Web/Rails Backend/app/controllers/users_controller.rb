class UsersController < ApplicationController
  before_action :authenticate, only: :create

  def create
    if (["admin","professor","student"].include?(params[:type]))
      is_admin = (params[:type] == "admin") ? 1:0
      @token = SecureRandom.uuid
      if @res = sign_up_query(params[:name], Digest::SHA1.hexdigest(params[:password]), params[:mail], is_admin, @token, params[:image_url] || "", (params["type"] == "professor") ? 1 : 0, params[:code] || "", params[:type], params[:book_limit])
        render "create"
      else
        render "layouts/error"
      end
    else
      render "layouts/error"
    end
  end

  def login
    if @res = login_query(params[:mail], Digest::SHA1.hexdigest(params[:password]), SecureRandom.uuid)
      render "login"
    else
      render "layouts/error"
    end
  end
end
