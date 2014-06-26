(ns isis.geom.action.helical-slice
  "The table of rules."
  (:require [isis.geom.action-dispatch :as master]
            [isis.geom.model.invariant :refer [marker->invariant?
                                               marker->add-invariant!]] ))



(defn helical->precondition?
  "Associated with each constraint type is a function which
  checks the preconditions and returns the marker which
  is underconstrained."
  [m1 m2 inv]
  (when (and (marker->invariant? inv m1 :z)
             (marker->invariant? inv m2 :z)
             (marker->invariant? inv m1 :x))
    m2))

(defmethod master/precondition?
  :helical
  [constraint invariants]
  (let [{m1 :m1 m2 :m2} constraint]
    (helical->precondition? m1 m2 invariants)))





(defn helical->postcondition!
  "Associated with each constraint type is a function which
  checks/sets the postconditions for once the constraint has been satisfied."
  [_ _ _])

(defmethod master/assert-postcondition!
  :helical
  [constraint invariants]
  (let [{m1 :m1 m2 :m2} constraint]
    (helical->postcondition! m1 m2 invariants)))