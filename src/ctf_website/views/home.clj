(ns ctf-website.views.home
  (:require [ctf-website.views.common :as common]
            [noir.session :as session])
  (:use [noir.core :only [defpage]]
        [hiccup.core :only [html]]))

(defpage
  "/" []
  (let [username (session/get :user)]
    (common/layout
      [:h1 "Austin 2600 CTF server"]
      (if (nil? username)
        [:a {:href "login"} "Compete"]
        [:form {:method "POST"
                :action "flag"}
         [:p (str "Submit flag as " username ":")
          [:input {:type "text"
                   :name "flag"}]
          [:input {:type "submit"
                   :value "Submit"}]]]))))
