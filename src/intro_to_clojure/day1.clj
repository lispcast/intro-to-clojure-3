(ns intro-to-clojure.day1
  (:require [bakery.robot :as robot]))

(defn scooped? [ingredient]
  (cond
    (= :flour ingredient)
    true
    (= :almond-milk ingredient)
    true
    (= :sugar ingredient)
    true
    (= :coconut-oil ingredient)
    true
    (= :baking-powder ingredient)
    true
    (= :corn-starch ingredient)
    true
    :else
    false))

(defn squeezed? [ingredient]
  (cond
    (= :lemon ingredient)
    true
    :else
    false))

(defn bowl-for [ingredient]
  (cond
    (= :flour ingredient)
    :dry
    (= :corn-starch ingredient)
    :dry
    (= :baking-powder ingredient)
    :dry
    (= :almond-milk ingredient)
    :wet
    (= :sugar ingredient)
    :wet
    (= :coconut-oil ingredient)
    :wet
    (= :lemon-juice ingredient)
    :wet
    :else
    (robot/error "I don't recognize" ingredient)))

(defn add-squeezed
  ([ingredient]
   (add-squeezed ingredient 1))
  ([ingredient quantity]
   (if (squeezed? ingredient)
     (dotimes [_ quantity]
       (robot/grab ingredient)
       (robot/squeeze (bowl-for ingredient))
       (robot/release))
     (robot/error ingredient "is not scooped."))))

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
     (robot/error ingredient "is not squeezed?"))))

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