(ns ctf-website.views.welcome
  (:require [ctf-website.views.common :as common])
  (:use [noir.core :only [defpage]]
        [hiccup.core :only [html]]))

(defpage "/welcome" []
         (common/layout
           [:p "Welcome to ctf-website"]))
