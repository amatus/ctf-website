(ns ctf-website.views.flag
  (:require [ctf-website.views.common :as common]
            [noir.session :as session])
  (:use [noir.core :only [defpage]]
        [hiccup.core :only [html]]))

(def no-session
  (common/layout
    [:p "You seem to have lost your session. Try to "
     [:a {:href "login"} "login"]
     " again."]))

(def good
  (common/layout
    [:p "Your flag was accepted."]))

(defpage
  [:post "/flag"] {:keys [flag]}
  (let [username (session/get :user)]
    (if (nil? username)
      no-session
      good)))
