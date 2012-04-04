(ns ctf-website.views.new
  (:require [ctf-website.views.common :as common])
  (:import java.lang.ProcessBuilder)
  (:use [noir.core :only [defpage]]
        [hiccup.core :only [html]]))

(defpage
  "/new" []
  (common/layout
    [:form {:method "POST"
            :action "new"}
     [:p "Username:"
      [:input {:type "text"
               :name "username"}]]
     [:p "Password:"
      [:input {:type "password"
               :name "password"}]]
     [:p
      [:input {:type "submit"
               :value "Create"}]]]))

(defpage
  [:post "/new"] {:keys [username password]}
  (let [adduser (.start (ProcessBuilder. (list "adduser" username)))
        _ (.close (.getOutputStream adduser))
        retval (.waitFor adduser)]
    (if (not (= 0 retval))
      (common/layout
        [:p "Try a username that doesn't suck"]
        [:p (str "result: " retval)])
      (let [chpasswd (.start (ProcessBuilder. (list "chpasswd")))
            out (.getOutputStream chpasswd)
            _ (.write out (.getBytes (str username ":" password) "UTF-8"))
            _ (.close out)
            retval (.waitFor chpasswd)]
        (common/layout
          [:p (str "result: " retval)])))))
