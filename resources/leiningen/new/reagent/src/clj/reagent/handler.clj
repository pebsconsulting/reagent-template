(ns {{name}}.handler
  (:require [reitit.ring :as reitit-ring]
            [{{name}}.middleware :refer [middleware]]
            [hiccup.page :refer [include-js include-css html5]]
            [config.core :refer [env]]))

(def mount-target
  [:div#app
   [:h2 "Welcome to {{name}}"]
   [:p "please wait while Figwheel is waking up ..."]
   [:p "(Check the js console for hints if nothing exсiting happens.)"]])

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   (include-css (if (env :dev) "/css/site.css" "/css/site.min.css"))])

(defn loading-page []
  (html5
   (head)
   [:body {:class "body-container"}
    mount-target
    (include-js "/js/app.js")]))
{{#devcards-hook?}}

(defn cards-page []
  (html5
    (head)
    [:body
     mount-target
     (include-js "/js/app_devcards.js")]))
{{/devcards-hook?}}

(defn index-handler
  [_request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (loading-page)})
 {{#devcards-hook?}}

(defn cards-handler
  [_]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (cards-page)})
{{/devcards-hook?}}

(def app
  (reitit-ring/ring-handler
   (reitit-ring/router
    [["/" {:get {:handler index-handler}}]
     ["/items"
      ["" {:get {:handler index-handler}}]
      ["/:item-id" {:get {:handler index-handler
                          :parameters {:path {:item-id int?}}}}]]
      {{#devcards-hook?}}
      ["/cards" {:get {:handler cards-handler}}]
      {{/devcards-hook?}}
     ["/about" {:get {:handler index-handler}}]]
    {:data {:middleware middleware}})
   (reitit-ring/routes
    (reitit-ring/create-resource-handler {:path "/" :root "/public"})
    (reitit-ring/create-default-handler))))
