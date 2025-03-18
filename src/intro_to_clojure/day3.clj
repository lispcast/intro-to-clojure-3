(ns intro-to-clojure.day3
  (:require [bakery.robot :as robot]))

(def pantry-ingredients #{:flour
                          :sugar
                          :corn-starch
                          :baking-powder
                          :cocoa
                          :coconut-oil})

(defn from-pantry? [ingredient]
  (contains? pantry-ingredients ingredient))

(def fridge-ingredients #{:almond-milk
                          :lemon})

(defn from-fridge? [ingredient]
  (contains? fridge-ingredients ingredient))

(def scooped-ingredients #{:flour
                           :almond-milk
                           :sugar
                           :coconut-oil
                           :baking-powder
                           :cocoa
                           :corn-starch})

(defn scooped? [ingredient]
  (contains? scooped-ingredients ingredient))

(comment

  (from-pantry? :cocoa)
  (scooped? :cocoa))

(def squeezed-ingredients #{:lemon})

(defn squeezed? [ingredient]
  (contains? squeezed-ingredients ingredient))

(def wet-ingredients #{:almond-milk :sugar :coconut-oil :lemon-juice :cocoa})
(def dry-ingredients #{:flour :corn-starch :baking-powder})

(defn bowl-for [ingredient]
  (cond
    (contains? wet-ingredients ingredient)
    :wet

    (contains? dry-ingredients ingredient)
    :dry

    :else
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

(defn bake-cake []
  (add :flour 2)
  (add :baking-powder)
  (add :almond-milk)
  (add :sugar)

  (robot/mix-bowl :dry)
  (robot/mix-bowl :wet)
  (robot/pour-bowl :wet :dry)
  (robot/mix-bowl :dry)
  (robot/pour-bowl :dry :pan)
  (robot/bake-pan 25)

  (robot/cool-pan))

(defn bake-cookies []
  (add :flour)
  (add :corn-starch)
  (add :sugar)
  (add :coconut-oil)

  (robot/mix-bowl :dry)
  (robot/mix-bowl :wet)
  (robot/pour-bowl :wet :dry)
  (robot/mix-bowl :dry)
  (robot/pour-bowl :dry :pan)
  (robot/bake-pan 30)

  (robot/cool-pan))

(defn bake-brownies []
  (add :coconut-oil 2)
  (add :cocoa 2)
  (add :sugar 1)
  (robot/mix-bowl :wet)

  (add :almond-milk)
  (robot/mix-bowl :wet)

  (add :flour 2)
  (robot/pour-bowl :wet :dry)
  (robot/mix-bowl :dry)
  (robot/pour-bowl :dry :pan)
  (robot/bake-pan 35)
  (robot/cool-pan))

(comment
  (robot/start-over)
  (bake-brownies)
  (robot/status))

(def cake-ingredients {:flour 2
                       :baking-powder 1
                       :almond-milk 1
                       :sugar 1})

(def cookie-ingredients {:flour 1
                         :corn-starch 1
                         :sugar 1
                         :coconut-oil 1})

(defn fetch-ingredient
  ([ingredient]
   (fetch-ingredient ingredient 1))
  ([ingredient amount]
   (cond
     (from-pantry? ingredient)
     (robot/go-to :pantry)
     (from-fridge? ingredient)
     (robot/go-to :fridge)
     :else
     (robot/error "I don't know where to find" ingredient))
   (dotimes [_ amount]
     (robot/load-up ingredient))
   (robot/go-to :prep-area)
   (dotimes [_ amount]
     (robot/unload ingredient))))

(defn fetch-ingredients [ingredient-list]
  ;; go to pantry
  (robot/go-to :pantry)
  ;; load up everything
  (doseq [ingredient-pair ingredient-list
          :let [ingredient-name (get ingredient-pair 0)
                ingredient-quantity (get ingredient-pair 1)]]
    (when (from-pantry? ingredient-name)
      (dotimes [_ ingredient-quantity]
        (robot/load-up ingredient-name))))

  ;; go to fridge
  (robot/go-to :fridge)
  ;; load up everything
  (doseq [ingredient-pair ingredient-list
          :let [ingredient-name (get ingredient-pair 0)
                ingredient-quantity (get ingredient-pair 1)]]
    (when (from-fridge? ingredient-name)
      (dotimes [_ ingredient-quantity]
        (robot/load-up ingredient-name))))

  ;; go to prep area
  (robot/go-to :prep-area)
  ;; unload everything
  (doseq [ingredient-pair ingredient-list
          :let [ingredient-name (get ingredient-pair 0)
                ingredient-quantity (get ingredient-pair 1)]]
    (dotimes [_ ingredient-quantity]
      (robot/unload ingredient-name))))

(defn add-ingredients [list1 list2]
  (merge-with + list1 list2))

(defn multiply-ingredients [quantity ingredient-list]
  (update-vals ingredient-list (fn [x] (* quantity x))))

(defn order->ingredients [order]
  (add-ingredients
   (multiply-ingredients (get (get order :items) :cake 0) cake-ingredients)
   (multiply-ingredients (get (get order :items) :cookies 0) cookie-ingredients)))

(defn orders->ingredients [orders]
  (reduce add-ingredients {} (map order->ingredients orders)))

(defn day-at-the-bakery []
  (let [orders (robot/get-morning-orders)
        ingredients (orders->ingredients orders)]
    (fetch-ingredients ingredients)
    (doseq [order orders]
      (let [items (get order :items)
            cake-count (get items :cake 0)
            cookies-count (get items :cookies 0)
            cooling-rack-ids (concat
                              (map (fn [_]
                                     (bake-cake))
                                   (range cake-count))
                              (map (fn [_]
                                     (bake-cookies))
                                   (range cookies-count)))]
        (robot/delivery {:orderid (get order :orderid)
                         :address (get order :address)
                         :rackids cooling-rack-ids})))))

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

(comment

  (perform {} [:cool])
  (perform {} [:mix :dry])
  (perform {} [:pour :wet :dry])
  (perform {} [:bake 45])

  (robot/start-over)
  (perform cake-ingredients [:add :all])
  (perform cake-ingredients [:add :flour])
  (perform cake-ingredients [:add :flour 3])
  (robot/status))