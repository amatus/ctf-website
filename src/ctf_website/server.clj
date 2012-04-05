(ns ctf-website.server
  (:require [noir.server :as server]))

(server/load-views "src/ctf_website/views/")

(defn redirect-https
  [handler]
  (fn [request]
    (if (= :http (:scheme request))
      (ring.util.response/redirect
        (str "https://" (:server-name request) (:uri request)))
      (handler request))))

(server/add-middleware redirect-https)

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "80"))]
    (server/start port {:mode mode
                        :jetty-options {:ssl? true
                                        :ssl-port 443
                                        :keystore "/home/ctf/ctf-website/key_crt.jks"
                                        :key-password "password"}
                        :ns 'ctf-website})))

