(ns isis.geom.action.offset-z-slice
  "The table of rules."
  (:require [isis.geom.position-dispatch :as master]
            [isis.geom.model.invariant :refer [marker->invariant?]] ))



(defn offset-z->precondition?
  "Associated with each constraint type is a function which
  checks the preconditions and returns the marker which
  is underconstrained."
  [m1 m2 inv]
  (when (marker->invariant? inv m1 :loc) m2))





