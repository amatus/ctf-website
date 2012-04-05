(ns ctf-website.views.home
  (:require [ctf-website.views.common :as common])
  (:use [noir.core :only [defpage]]
        [hiccup.core :only [html]]))

(defpage "/" []
         (common/layout
           [:h1 "Austin 2600 CTF server"]
           [:a {:href "login"} "Compete"]))
