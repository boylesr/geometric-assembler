(ns isis.geom.machine.tolerance)

(def ^:dynamic *default-tolerance* 0.001)

(defn near-equal?
  "A function which takes a set of scalar arguments
  and compares them making sure that they are within
  the specified tolerance."
  [tol-value & xs]
  (let [[amin amax] (apply (juxt min max) xs)]
    (< (- amax amin)
       (cond (= :default tol-value) *default-tolerance*
                :else tol-value))))


(defn near-zero?
  "A function which takes a scalar argument
  and verifies that its magnitude is sufficiently small."
  [tol-value x]
  (< ((if (pos? x) + -) x)
     (cond (= :default tol-value) *default-tolerance*
           (= :tiny tol-value) 1e-10
           :else tol-value)))

(defn near-same?
  "A function which compares a set of objects.
  They must all be of the same type and there
  values must be individually in range."
  [tol-value & xs]
  (if (not= (apply map #(count %) xs))
    false
    (every? identity (apply map #(apply near-equal? tol-value %&) xs))))

