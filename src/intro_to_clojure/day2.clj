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