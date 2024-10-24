(ns intro-to-clojure.day2
  (:require [bakery.robot :as robot]
            [intro-to-clojure.day1 :as day1]))

(comment
  (robot/go-to :pantry)
  (robot/load-up :flour)
  (robot/go-to :prep-area)
  (robot/unload :flour)


  (robot/status))