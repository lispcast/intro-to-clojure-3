(ns intro-to-clojure.day3
  (:require [bakery.robot :as robot]
            [clojure.edn :as edn]
            [clojure.pprint :as pprint]))

(def database (edn/read-string (slurp "bakery.edn")))

(comment

  (spit "bakery2.edn" (pr-str database))

  (spit "bakery2.edn" (with-out-str (pprint/pprint database))))

(defn bowl-for [ingredient]
  (or
   (get-in database [:ingredients ingredient :bowl])
   (robot/error "I don't recognize" ingredient)))

(defn add-squeezed
  ([ingredient]
   (add-squeezed ingredient 1))
  ([ingredient quantity]
   (if (= :squeezed (get-in database [:ingredients ingredient :add-method]))
     (dotimes [_ quantity]
       (robot/grab ingredient)
       (robot/squeeze (bowl-for ingredient))
       (robot/release))
     (robot/error ingredient "is not squeezed"))))

(defn add-scooped
  ([ingredient]
   (add-scooped ingredient 1))
  ([ingredient quantity]
   (if (= :scooped (get-in database [:ingredients ingredient :add-method]))
     (do
       (robot/grab :cup)
       (dotimes [_ quantity]
         (robot/scoop ingredient)
         (robot/dump (bowl-for ingredient)))
       (robot/release))
     (robot/error ingredient "is not scooped"))))

(def add-functions {:scooped  add-scooped
                    :squeezed add-squeezed})

(defn add
  ([ingredient]
   (add ingredient 1))
  ([ingredient quantity]
   (let [add-method (get-in database [:ingredients ingredient :add-method])
         add-function (get add-functions add-method (fn [ingredient _quantity]
                                                      (robot/error "I don't know how to add" ingredient)))]
     (add-function ingredient quantity))))

(defn fetch-ingredient
  ([ingredient]
   (fetch-ingredient ingredient 1))
  ([ingredient amount]
   (robot/go-to (get-in database [:ingredients ingredient :location]))
   (dotimes [_ amount]
     (robot/load-up ingredient))
   (robot/go-to :prep-area)
   (dotimes [_ amount]
     (robot/unload ingredient))))

(defn fetch-ingredients [ingredient-list]
  (doseq [location [:pantry :fridge]]
    (robot/go-to location)
    (doseq [[ingredient quantity] ingredient-list
            :when (= location (get-in database [:ingredients ingredient :location]))
            _ (range quantity)]
      (robot/load-up ingredient)))

  ;; go to prep area
  (robot/go-to :prep-area)
  ;; unload everything
  (doseq [[ingredient quantity] ingredient-list
          _ (range quantity)]
    (robot/unload ingredient)))

(defn add-ingredients [list1 list2]
  (merge-with + list1 list2))

(defn multiply-ingredients [quantity ingredient-list]
  (update-vals ingredient-list (fn [x] (* quantity x))))

(defn order->ingredients [order]
  (->> (:items order)
       (map (fn [[item quantity]]
              (multiply-ingredients quantity (get-in database [:recipes item :ingredients]))))
       (reduce add-ingredients {})))

(defn orders->ingredients [orders]
  (->> orders
       (map order->ingredients)
       (reduce add-ingredients {})))

(def perform-functions {:cool (fn [_ingredients _ _]
                                (robot/cool-pan))
                        :mix (fn [_ingredients bowl _]
                               (robot/mix-bowl bowl))
                        :pour (fn [_ingredients from to]
                                (robot/pour-bowl from to))
                        :bake (fn [_ingredients time _]
                                (robot/bake-pan time))
                        :add (fn [ingredients ingredient quantity]
                               (if (= :all ingredient)
                                 (doseq [[ingredient amount] ingredients]
                                   (add ingredient amount))
                                 (add ingredient (or quantity (get ingredients ingredient)))))})

(defn perform [ingredients step]
  (let [operation (first step)
        perform-function (get perform-functions operation)]
    (perform-function ingredients (second step) (get step 2))))

(defn bake-recipe [recipe]
  (let [ingredients (:ingredients recipe)
        steps       (:steps       recipe)]
    (last (map (fn [step] (perform ingredients step)) steps))))

(defn day-at-the-bakery []
  (let [orders (robot/get-morning-orders)
        ingredients (orders->ingredients orders)]
    (fetch-ingredients ingredients)
    (doseq [order orders]
      (let [cooling-rack-ids (->> (:items order)
                                  (mapcat (fn [[item quantity]] (repeat quantity item)))
                                  (map (fn [item] (bake-recipe (get-in database [:recipes item])))))]
        (robot/delivery {:orderid (:orderid order)
                         :address (:address order)
                         :rackids cooling-rack-ids})))))

(comment
  (->> {:cake 4 :cookies 3}
       (mapcat (fn [[item quantity]] (repeat quantity item)))
       (map (fn [item] (bake-recipe (get-in database [:recipes item]))))))