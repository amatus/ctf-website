(ns ctf-website.models.flags
  (:require [simpledb.core :as db]
            [ctf-website.models.scoreboard :as scoreboard]))

(defn init! []
  (db/put! :flags {}))

(defn submit! [username flag]
  (let [flag-info (get (db/get :flags) flag)]
    (if (nil? flag-info)
      false
      (do
        (scoreboard/update! username flag-info)
        true))))
