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
