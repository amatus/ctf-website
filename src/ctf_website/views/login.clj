(ns ctf-website.views.login
  (:require [ctf-website.views.common :as common])
  (:import net.sf.jpam.Pam)
  (:use [noir.core :only [defpage]]
        [hiccup.core :only [html]]))

(defpage [:post "/login"] {:keys [username password]}
         (let [pam (Pam.)
               authenticated (.authenticateSuccessful pam username password)]
           (if authenticated
             (common/layout
               [:p "You're in"])
             (common/layout
               [:p "Go away"]))))

(defpage "/login" []
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
