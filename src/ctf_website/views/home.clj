(ns ctf-website.views.home
  (:require [ctf-website.views.common :as common])
  (:use [noir.core :only [defpage]]
        [hiccup.core :only [html]]))

(defpage "/" []
         (common/layout
           [:p "Welcome to ctf-website"]))
