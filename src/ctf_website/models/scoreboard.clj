(ns ctf-website.models.scoreboard
  (:import java.util.Date)
  (:require [simpledb.core :as db]))

(defn init! []
  (db/put! :scoreboard {}))

(defn update! [username flag-info]
  (db/update!
    :scoreboard
    (fn [board]
      (assoc-in board [username flag-info] (str (Date.))))))

(defn get-scores []
  (db/get :scoreboard))

(defn has [username flag-info]
  (not (nil? (get (get (db/get :scoreboard) username) flag-info))))
