;; my solution

(defn add
  ([ingredient]
   (add ingredient 1))
  ([ingredient n]
   (cond
     (squeezed? ingredient)
     (add-squeezed ingredient n)
     
     (scooped? ingredient)
     (add-scooped ingredient n)
     
     :else
     (error "I don't know how to add" ingredient))))