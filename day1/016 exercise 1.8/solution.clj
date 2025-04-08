;; my solution

(defn add-flour-cups [n]
  (dotimes [_ n]
    (add :flour))
  :ok)

(defn add-sugar-cups [n]
  (dotimes [_ n]
    (add :sugar))
  :ok)

(defn add-almond-milk-cups [n]
  (dotimes [_ n]
    (add :almond-milk))
  :ok)

(defn add-baking-powder-cups [n]
  (dotimes [_ n]
    (add :baking-powder))
  :ok)