(ns ctf-website.views.home
  (:require [ctf-website.views.common :as common]
            [ctf-website.models.scoreboard :as scoreboard]
            [noir.session :as session])
  (:use [noir.core :only [defpage defpartial]]
        [hiccup.core :only [h]]))

(defn sorted-flags [scores]
  (sort (set (mapcat keys (vals scores)))))

(defn sorted-users [scores]
  (sort (keys scores)))

(defpartial
  table-header [flag]
  [:th flag])

(defpartial
  table-cell [scores user flag]
  [:td (get (get scores user) flag)])

(defpartial
  table-row [scores flags user]
  [:tr
   [:td user]
   (map (partial table-cell scores user) flags)])

(defpartial
  scoreboard-table [scores]
  (let [flags (sorted-flags scores)
        users (sorted-users scores)]
    [:table {:border "1"}
     [:thead
      [:th "Competitor"]
      (map table-header flags)]
     [:tbody
      (map (partial table-row scores flags) users)]]))

(defpage
  "/" []
  (let [username (session/get :user)]
    (common/layout
      [:h1 "Austin 2600 CTF server"]
      (scoreboard-table (scoreboard/get-scores))
      (if (nil? username)
        [:a {:href "login"} "Compete"]
        [:form {:method "POST"
                :action "flag"}
         [:p (str "Submit flag as " (h username) ":")
          [:input {:type "text"
                   :name "flag"
                   :value (when (not (scoreboard/has username "test"))
                            "b46911b2d927d89bc2b1143a7f5d9c20")}]
          [:input {:type "submit"
                   :value "Submit"}]]]))))
