(ns ctf-website.models
  (:require [simpledb.core :as db]
            [ctf-website.models.flags :as flags]
            [ctf-website.models.scoreboard :as scoreboard]))

(defn initialize []
  (db/init)
  (when-not (db/get :flags)
    (flags/init!)
    (scoreboard/init!)))
