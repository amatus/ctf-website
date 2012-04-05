(ns ctf-website.views.flag
  (:require [ctf-website.views.common :as common]
            [ctf-website.models.flags :as flags]
            [noir.session :as session])
  (:use [noir.core :only [defpage]]
        [hiccup.page-helpers :only [link-to]]))

(defpage
  [:post "/flag"] {:keys [flag]}
  (common/layout
    (let [username (session/get :user)]
      (if (nil? username)
        [:p "You seem to have lost your session. Try to "
         (link-to "login" "login") " again."]
        (if (flags/submit! username flag)
          [:p "Your flag was accepted."]
          [:p "Your flag was rejected."])))))
