;; code we develop in the lesson

(defn add-corn-starch-cups [n]
  (dotimes [i n]
    (add :corn-starch))
  :ok)

(defn add-coconut-oil-cups [n]
  (dotimes [_ n]
    (add :coconut-oil))
  :ok)

(defn add-lemons [n]
  (dotimes [_ n]
    (add :lemon))
  :ok)