(ns intro-to-clojure.day2
  (:require [bakery.robot :as robot]
            [intro-to-clojure.day1 :as day1]))

(comment
  (robot/go-to :pantry)
  (robot/load-up :flour)
  (robot/go-to :prep-area)
  (robot/unload :flour)


  (robot/status))

(comment

  #{1 2 3}
  #{1 2 :hello "hello"}

  (contains? #{1 2 3} :hello)

  #{1 2 3 4 5 6 7 8 9})

(def pantry-ingredients #{:flour
                          :sugar
                          :corn-starch
                          :baking-powder
                          :coconut-oil})

(defn from-pantry? [ingredient]
  (contains? pantry-ingredients ingredient))

(comment
  (from-pantry? :sugar)
  (from-pantry? :lemon))

(def fridge-ingredients #{:almond-milk
                          :lemon})

(defn from-fridge? [ingredient]
  (contains? fridge-ingredients ingredient))

(comment
  (from-fridge? :flour)
  (from-fridge? :lemon))

(def scooped-ingredients #{:flour
                           :almond-milk
                           :sugar
                           :coconut-oil
                           :baking-powder
                           :corn-starch})

(defn scooped? [ingredient]
  (contains? scooped-ingredients ingredient))

(def squeezed-ingredients #{:lemon})

(defn squeezed? [ingredient]
  (contains? squeezed-ingredients ingredient))

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

(comment
  (robot/status)
  (robot/start-over)
  (fetch-ingredient :flour 10)
  (fetch-ingredient :almond-milk 20)
  (fetch-ingredient :fdsfsfs)

  (fetch-ingredients {:flour 10
                      :sugar 22
                      :almond-milk 2}))

(comment

  {}
  {:name "Eric"
   :age 43
   :age-next-year 44
   :a 4
   :b 5
   :c 6
   :d 8
   :e 9
   :f 10
   :g 11
   :y 12}

  [1 2 3 4]
  (rest (rest (rest (rest (seq [1 2 3 4])))))
  (first [1 2 3 4])
  (first (rest #{1 2 3 4 5 6}))
  (seq {:a 1 :b 2 :c 3})
  (seq "Hello, World!")

  (doseq [ingredient fridge-ingredients]
    (println "I like" ingredient)))

(comment

  (let [orders (robot/get-morning-orders)
        order (first orders)
        items (get order :items)]
    (println order)
    (doseq [item-pair items]
      (let [item-name (get item-pair 0)
            item-count (get item-pair 1)]
        (dotimes [_ item-count])))))

(defn fetch-for-cake []
  (fetch-ingredient :flour 2)
  (fetch-ingredient :baking-powder)
  (fetch-ingredient :almond-milk)
  (fetch-ingredient :sugar))

(defn fetch-for-cookies []
  (fetch-ingredient :flour)
  (fetch-ingredient :corn-starch)
  (fetch-ingredient :sugar)
  (fetch-ingredient :coconut-oil))

(comment
  (fetch-for-cake)
  (fetch-for-cookies))

(defn day-at-the-bakery []
  ;; get-morning-orders
  (let [orders (robot/get-morning-orders)]
    (doseq [order orders
            item-pair (get order :items)
            :let [item-name (get item-pair 0)
                  item-count (get item-pair 1)]]
      (dotimes [_ item-count]
        (cond
          (= :cake item-name)
          (do
            (fetch-for-cake)
            (let [cooling-rack-id (day1/bake-cake)]
              (robot/delivery {:orderid (get order :orderid)
                               :address (get order :address)
                               :rackids [cooling-rack-id]})))
          (= :cookies item-name)
          (do
            (fetch-for-cookies)
            (let [cooling-rack-id (day1/bake-cookies)]
              (robot/delivery {:orderid (get order :orderid)
                               :address (get order :address)
                               :rackids [cooling-rack-id]})))
          :else
          (robot/error "I don't know how to fetch ingredients for" item-name))))))

(comment
  (robot/start-over)
  (day-at-the-bakery)
  (day1/bake-cake))