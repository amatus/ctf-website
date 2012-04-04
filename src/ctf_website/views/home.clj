(ns ctf-website.views.home
  (:require [ctf-website.views.common :as common])
  (:use [noir.core :only [defpage]]
        [hiccup.core :only [html]]))

(defpage "/" []
         (common/layout
           [:h1 "Austin 2600 CTF server"]
           [:a {:href "new"} "Create account"]
           [:form {:method "POST"
                   :action "login"}
            [:p "Username:"
             [:input {:type "text"
                      :name "username"}]]
            [:p "Password:"
             [:input {:type "password"
                      :name "password"}]]
            [:p
             [:input {:type "submit"
                      :value "Login"}]]]))
