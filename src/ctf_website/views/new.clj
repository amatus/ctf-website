(ns ctf-website.views.new
  (:require [ctf-website.views.common :as common])
  (:import java.lang.ProcessBuilder
           [java.util.concurrent Executors TimeUnit])
  (:use [noir.core :only [defpage]]
        [hiccup.core :only [html]]))

(defpage
  "/new" []
  (common/layout
    [:form {:method "POST"
            :action "new"}
     [:p "Username:"
      [:input {:type "text"
               :name "username"}]]
     [:p "Password:"
      [:input {:type "password"
               :name "password"}]]
     [:p
      [:input {:type "submit"
               :value "Create"}]]]))

(def fail
  (common/layout
    [:p "Try a username that doesn't suck."]))

(def throttled
  (common/layout
    [:p "Woah woah woah! Let's not all talk at once."]))

(def good
  (ring.util.response/redirect "login"))

(defonce *barrier* (ref true))
(defonce *timer* (Executors/newScheduledThreadPool 1))

(defpage
  [:post "/new"] {:keys [username password]}
  (let [throttle (ref true)]
    (dosync
      (when (deref *barrier*)
        (ref-set throttle false)
        (ref-set *barrier* false)))
    (if (deref throttle)
      throttled
      (let [_ (.schedule *timer* #(dosync (ref-set *barrier* true))
                         1 (TimeUnit/SECONDS))
            adduser (.start (ProcessBuilder. (list "adduser" username)))
            _ (.close (.getOutputStream adduser))
            retval (.waitFor adduser)]
        (if (not (= 0 retval))
          fail ;; this seems to take care of usernames containing : or \n
          (let [chpasswd (.start (ProcessBuilder. (list "chpasswd")))
                out (.getOutputStream chpasswd)
                userpass (.getBytes (str username ":" password) "UTF-8")
                ;; chpasswd seems to only care about \n, though I only tested
                ;; \n and \r and \0.
                userpass (remove #(= 0x0a %) userpass)
                _ (.write out (into-array Byte/TYPE userpass))
                _ (.close out)
                retval (.waitFor chpasswd)]
            good))))))
