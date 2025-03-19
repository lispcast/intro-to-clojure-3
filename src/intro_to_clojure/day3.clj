(ns intro-to-clojure.day3
  (:require [bakery.robot :as robot]))

(def database {:recipes {:cake {:ingredients {:flour 2
                                              :baking-powder 1
                                              :almond-milk 1
                                              :sugar 1}
                                :steps [[:add :flour]
                                        [:add :baking-powder]
                                        [:add :almond-milk]
                                        [:add :sugar]

                                        [:mix :dry]
                                        [:mix :wet]
                                        [:pour :wet :dry]
                                        [:mix :dry]
                                        [:pour :dry :pan]
                                        [:bake 25]

                                        [:cool]]}

                         :cookies {:ingredients {:flour 1
                                                 :corn-starch 1
                                                 :sugar 1
                                                 :coconut-oil 1}
                                   :steps [[:add :flour]
                                           [:add :corn-starch]
                                           [:add :sugar]
                                           [:add :coconut-oil]

                                           [:mix :dry]
                                           [:mix :wet]
                                           [:pour :wet :dry]
                                           [:mix :dry]
                                           [:pour :dry :pan]
                                           [:bake 30]

                                           [:cool]]}

                         :brownies {:ingredients {:coconut-oil 2
                                                  :cocoa 2
                                                  :sugar 1
                                                  :almond-milk 1
                                                  :flour 2}
                                    :steps [[:add :coconut-oil]
                                            [:add :cocoa]
                                            [:add :sugar]
                                            [:mix :wet]

                                            [:add :almond-milk]
                                            [:mix :wet]

                                            [:add :flour]
                                            [:pour :wet :dry]
                                            [:mix :dry]
                                            [:pour :dry :pan]
                                            [:bake 35]
                                            [:cool]]}}
               :ingredients {:flour         {:location :pantry
                                             :add-method :scooped
                                             :bowl :dry}
                             :sugar         {:location :pantry
                                             :add-method :scooped
                                             :bowl :wet}
                             :corn-starch   {:location :pantry
                                             :add-method :scooped
                                             :bowl :dry}
                             :baking-powder {:location :pantry
                                             :add-method :scooped
                                             :bowl :dry}
                             :cocoa         {:location :pantry
                                             :add-method :scooped
                                             :bowl :wet}
                             :coconut-oil   {:location :pantry
                                             :add-method :scooped
                                             :bowl :wet}
                             :almond-milk   {:location :fridge
                                             :add-method :scooped
                                             :bowl :wet}
                             :lemon         {:location :fridge
                                             :add-method :squeezed
                                             :bowl :wet}}})

(comment)

(defn scooped? [ingredient]
  (= :scooped (get-in database [:ingredients ingredient :add-method])))

(comment
  (scooped? :cocoa))

(defn squeezed? [ingredient]
  (= :squeezed (get-in database [:ingredients ingredient :add-method])))

(defn bowl-for [ingredient]
  (or
   (get-in database [:ingredients ingredient :bowl])
   (robot/error "I don't recognize" ingredient)))

(comment
  (bowl-for :cocoa))

(defn add-squeezed
  ([ingredient]
   (add-squeezed ingredient 1))
  ([ingredient quantity]
   (if (squeezed? ingredient)
     (dotimes [_ quantity]
       (robot/grab ingredient)
       (robot/squeeze (bowl-for ingredient))
       (robot/release))
     (robot/error ingredient "is not squeezed"))))

(defn add-scooped
  ([ingredient]
   (add-scooped ingredient 1))
  ([ingredient quantity]
   (if (scooped? ingredient)
     (do
       (robot/grab :cup)
       (dotimes [_ quantity]
         (robot/scoop ingredient)
         (robot/dump (bowl-for ingredient)))
       (robot/release))
     (robot/error ingredient "is not scooped"))))

(defn add
  ([ingredient]
   (add ingredient 1))
  ([ingredient quantity]
   (cond
     (squeezed? ingredient)
     (add-squeezed ingredient quantity)

     (scooped? ingredient)
     (add-scooped ingredient quantity)

     :else
     (robot/error "I don't know how to add" ingredient))))

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

(comment
  (robot/start-over)
  (fetch-ingredient :flour 10)
  (robot/status))

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

(comment
  (fetch-ingredients (get-in database [:recipes :cookies :ingredients])))

(defn add-ingredients [list1 list2]
  (merge-with + list1 list2))

(defn multiply-ingredients [quantity ingredient-list]
  (update-vals ingredient-list (fn [x] (* quantity x))))

(defn order->ingredients [order]
  (reduce add-ingredients
          {}
          (map (fn [[item quantity]]
                 (multiply-ingredients quantity (get-in database [:recipes item :ingredients])))
               (:items order))))

;; -> ->>

(comment

  (order->ingredients {:orderid 6864, :address "543 Servo St", :items {:cake 18, :cookies 10}}))

(defn orders->ingredients [orders]
  (reduce add-ingredients {} (map order->ingredients orders)))

(defn perform [ingredients step]
  (let [operation (first step)]
    (cond
      (= :cool operation)
      (robot/cool-pan)

      (= :mix operation)
      (robot/mix-bowl (second step))

      (= :pour operation)
      (robot/pour-bowl (second step) (get step 2))

      (= :bake operation)
      (robot/bake-pan (second step))

      (= :add operation)
      (cond
        (= :all (second step))
        (doseq [ingredient-pair ingredients]
          (let [ingredient (key ingredient-pair)
                amount     (val ingredient-pair)]
            (add ingredient amount)))

        (= 2 (count step))
        (add (second step) (get ingredients (second step)))

        (= 3 (count step))
        (add (second step) (get step 2))))))

(defn bake-recipe [recipe]
  (let [ingredients (:ingredients recipe)
        steps       (:steps       recipe)]
    (last (map (fn [step] (perform ingredients step)) steps))))

(defn day-at-the-bakery []
  (let [orders (robot/get-morning-orders)
        ingredients (orders->ingredients orders)]
    (fetch-ingredients ingredients)
    (doseq [order orders]
      (let [items (:items order)
            cooling-rack-ids (mapcat (fn [[item quantity]]
                                       (map (fn [_]
                                              (bake-recipe (get-in database [:recipes item])))
                                            (range quantity)))
                                     items)]
        (prn cooling-rack-ids)
        (robot/delivery {:orderid (:orderid order)
                         :address (:address order)
                         :rackids cooling-rack-ids})))))

(comment
  (day-at-the-bakery)

  (map range [1 2 3])
  (mapcat range [1 2 3]))

(comment

  (perform {} [:cool])
  (perform {} [:mix :dry])
  (perform {} [:pour :wet :dry])
  (perform {} [:bake 45])

  (robot/start-over)

  (bake-recipe (get-in database [:recipes :cookies]))
  (robot/status))