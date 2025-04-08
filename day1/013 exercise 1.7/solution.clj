;; my solution

(defn add-squeezed [ingredient bowl]
  (if (squeezed? ingredient)
    (do
      (grab ingredient)
      (squeeze bowl)
      (release))
    (error ingredient "is not squeezed")))

(defn add-scooped [ingredient bowl]
  (if (scooped? ingredient)
    (do
      (grab :cup)
      (do
        (scoop ingredient)
        (dump bowl))
      (release))
    (error ingredient "is not scooped")))

(defn bake-cake []
  (add-flour-cups 2)
  (add-baking-powder-cups 1)
  (add-almond-milk-cups 1)
  (add-sugar-cups 1)
  
  (mix-bowl :dry)
  (mix-bowl :wet)
  (pour-bowl :wet :dry)
  (mix-bowl :dry)
  (pour-bowl :dry :pan)
  (bake-pan 25)
  (cool-pan))