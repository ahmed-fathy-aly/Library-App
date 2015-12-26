class ReservationsController < ApplicationController
    before_action :authenticate

    def index
      @reservations = get_all_reservations(@token)
    end

    def lend
      if is_admin?(@token) && lend_book(@token, params[:reservation_code])
        render "layouts/successful"
      else
        render "layouts/error"
      end
    end

    def return
      if is_admin?(@token) && return_book(@token, params[:reservation_code])
        render "layouts/successful"
      else
        render "layouts/error"
      end
    end
end
