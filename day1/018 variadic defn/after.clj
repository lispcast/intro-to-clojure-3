;; The code we develop in this lesson

(defn add-scooped
  ([ingredient]
   (add-scooped ingredient 1))
  ([ingredient n]
   (if (scooped? ingredient)
     (do
       (grab :cup)
       (dotimes [_ n]
         (scoop ingredient)
         (dump (bowl-for ingredient)))
       (release))
     (error ingredient "is not scooped."))))

(defn add-squeezed
  ([ingredient]
   (add-squeezed ingredient 1))
  ([ingredient n]
   (if (squeezed? ingredient)
     (dotimes [_ n]
       (grab ingredient)
       (squeeze (bowl-for ingredient))
       (release))
     (error ingredient "is not squeezed."))))