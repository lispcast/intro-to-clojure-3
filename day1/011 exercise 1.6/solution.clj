;; my solution

(defn squeezed? [ingredient]
  (cond
    (= :lemon ingredient)
    true
    :else
    false))