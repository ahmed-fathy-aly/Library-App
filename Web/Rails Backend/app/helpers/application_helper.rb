module ApplicationHelper
  def envelope(json, status)
      json.status status
      json.response do
          yield if block_given?
      end
  end
end
