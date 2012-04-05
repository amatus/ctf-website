(ns ctf-website.views.common
  (:use [noir.core :only [defpartial]]
        [hiccup.page-helpers :only [include-css html5]]))

(defpartial layout [& content]
            (html5
              [:head
               [:title "ctf-website"]]
              [:body
               [:div#wrapper
                content]]))
