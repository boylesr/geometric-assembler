(ns isis.geom.pft.coincident_slice
  (:require isis.geom.machine.auxiliary-procs
            isis.geom.analysis.analysis-utilities))


;; PFT entry: ALL
(def pft-coincident
  {:constraint  '(coincident ?m-1, ?m-2)

   :assumptions
   (marker-has-invariant-position ?m-1)
   (geom-has-marker ?link  ?m-2)
   } )


;; PFT entry: (0,0,coincident)
(def pft-0-0-coincident
  {:parent pft-coincident

   :initial-state
   [
    '(0-TDOF ?link, ?point)
    '(0-RDOF ?link)
    ]

   :fragment
   (fn [?m-1 ?m-2]
     (if-not (zero? (vec-diff ((gmp ?m-1) (gmp ?m-2))))
         (error r0 estring-9) ))

   :new-state
   [
    '(0-TDOF ?link, ?point)
    '(0-RDOF ?link)
    ]
   }

   :explanation
   "Geom ?link is fixed, so the coincident constraint
   can only be checked for consistency."
  )


"
---
PFT entry: (1,0,coincident)
---

Initial status:
  1-TDOF(?link, ?point, ?line, ?lf)
  0-RDOF(?link)

Plan fragment:
  begin
  R[0] = vec-diff(gmp(?m-1), gmp(?m-2));
  translate(?link, R[0]);
  R[1] = perp-dist(?point, ?line);
  unless zero?(R[1])
    error(R[1], estring[1]);
  end;

New status:
  0-TDOF(?link, ?point)
  0-RDOF(?link)

Explanation:
  Geom ?link cannot rotate.
  Therefore, the coincident constraint must be satisfied by a
  translation by the vector from marker ?m-2 to marker ?m-1.
  This translation may invalidate the constraint that ?point
  remain on ?line; thus, a run-time check is required.


---
PFT entry: (2,0,coincident)
---

Initial status:
  2-TDOF(?link, ?point, ?line, ?lf)
  0-RDOF(?link)

Plan fragment:
  begin
  R[0] = vec-diff(gmp(?m-1), gmp(?m-2));
  translate(?link, R[0]);
  R[1] = perp-dist(?point, ?plane);
  unless zero?(R[1])
    error(R[1], estring[1]);
  end;

New status:
  0-TDOF(?link, ?point)
  0-RDOF(?link)

Explanation:
  Geom ?link cannot rotate.
  Therefore, the coincident constraint must be satisfied by a
  translation by the vector from marker ?m-2 to marker ?m-1.
  This translation may invalidate the constraint that ?point
  remain on ?plane; thus, a run-time check is required.


---
PFT entry: (3,0,coincident)
---

Initial status:
  3-TDOF(?link)
  0-RDOF(?link)

Plan fragment:
  begin
  translate(?link,
    vec-diff(gmp(?m-1), gmp(?m-2)));
  R[0] = gmp(?m-2);
  end;

New status:
  0-TDOF(?link, R[0])
  0-RDOF(?link)

Explanation:
  Geom ?link is free to translate,
  so the translation vector is measured and the geom is moved.
  No checks are required.

---
PFT entry: (h,h,coincident)
---

Initial status:
  h-TDOF(?link, ?point, ?line, ?point)
  h-RDOF(?link, ?axis, ?axis_1, ?axis_2)

Plan fragment:
  begin
  R[0] = vec-diff(gmp(?m-1), gmp(?m-2));
  translate(?link, R[0]);
  R[1] = pc-check(?line, R[0], nil, 0);
  unless R[1]
    error(pc-error((?line, R[0], nil), estring[4]);
  if null?(?axis_1) and null?(?axis_2)
    then rotate(?link, ?point, ?axis, R[1])
    else 2r/n(?link, ?point, R[1], ?axis, ?axis_1, ?axis_2);
  end;

New status:
  0-TDOF(?link, ?point)
  0-RDOF(?link)

Explanation:
  Geom ?link is translated along ?line and rotated about ?axis.
  The rotation must be consistent with the translation
  and ?line parameters.




---
PFT entry: (1,1,coincident)
---

Initial status:
  1-TDOF(?link, ?point, ?line, ?lf)
  1-RDOF(?link, ?axis, ?axis_1, ?axis_2)

Branch variables:
  q_0, denoting a 2-way branch

Plan fragment:
  begin
  1t-1r/p-p(?link, ?point, ?line,
    ?axis, ?axis_1, ?axis_2,
    gmp(?m-2), gmp(?m-1), ?lf, q_0);
  end;

New status:
  0-TDOF(?link, ?point)
  0-RDOF(?link)

Explanation:
  Geom ?link has only one rotational and one translational degree of freedom.
  Therefore it must be translated along and rotated about
  the ?line to which ?point is constrained.
  This effect is achieved by translating ?link to make the
  markers coincident, and then attempting to move ?point back onto ?line,
  using a rotation.
  In general, there are two distinct solutions to this problem,
  so a branch variable q_0 is used to select the desired solution.



---
PFT entry: (2,1,coincident)
---

Initial status:
  2-TDOF(?link, ?point, ?plane, ?lf)
  1-RDOF(?link, ?axis, ?axis_1, ?axis_2)

Branch variables:
  q_0, denoting a 2-way branch

Plan fragment:
  begin
  2t-1r/p-p(?link, ?point, ?plane,
    ?axis, ?axis_1, ?axis_2,
    gmp(?m-2), gmp(?m-1), ?lf, q_0);
  end;

New status:
  0-TDOF(?link, ?point)
  0-RDOF(?link)

Explanation:
  Geom ?link has one rotational and two translational degrees of freedom.
  Therefore it must be translated along its
  known ?plane and rotated about its known ?axis.
  This effect is achieved by translating ?link to make the
  markers coincident, and then attempting to move ?point back onto ?plane.
  In general, there are two distinct solutions to this problem,
  so a branch variable q_0 is used to select the desired solution.


---
PFT entry: (3,1,coincident)
---

Initial status:
  3-TDOF(?link)
  1-RDOF(?link, ?axis, ?axis_1, ?axis_2)

Plan fragment:
  begin
  translate(?link,
    vec-diff(gmp(?m-1), gmp(?m-2)));
  R[0] = gmp(?m-2);
  end;

New status:
  0-TDOF(?link, R[0])
  1-RDOF(?link, ?axis, ?axis_1, ?axis_2)

Explanation:
  Geom ?link is free to translate, so the translation
  vector is measured and the geom is moved.
  No checks are required.


---
PFT entry: (0,2,coincident)
---

Initial status:
  0-TDOF(?link, ?point)
  2-RDOF(?link, ?axis_1, ?axis_2)

Branch variables:
  q_0, denoting a 2-way branch

Plan fragment:
  begin
  2r/p-p(?link, ?point,
    gmp(?m-2), gmp(?m-1),
    ?axis_1, ?axis_2, q_0);
  end;

New status:
  0-TDOF(?link, ?point)
  0-RDOF(?link)

Explanation:
  Geom ?link has two rotational degrees of fredom.
  The marker that is to be moved is first placed in a plane
  through the desired locatoins and perpendicular to ?axis_1,
  by rotation aboutn ?axis_2.
  It is then put into final position by rotation about ?axis_1.
  This action has a potential degeneracy:
  If either ?axis_1 or ?axis_2 lins up with the
  vector from ?point to ?m-2,
  then there will still be a rotational degree of freedom.




---
PFT entry: (1,3,coincident)
---

Initial status:
  1-TDOF(?link, ?point, ?line, ?lf)
  3-RDOF(?link)

Branch variables:
  q_0, denoting a 2-way branch

Plan fragment:
  begin
  1t-3r/p-p(?link, ?point, ?line,
    gmp(?m-2), gmp(?m-1),
    ?lf, q_0);
  R[1] = vec-diff(gmp(?m-2), ?point);
  end;

New status:
  0-TDOF(?link, ?point)
  1-RDOF(?link, R[1], nil, nil)

Explanation:
  Geom ?link is translated to bring ?m-2 into
  coincidence with ?m-1.
  Then, ?link is rotated to bring ?point back onto ?line.
  In general, there are two distinct solutions to
  this problem, so a branch variable q_0 is used to
  select the desired solution.
  After the constraint is satisfied, ?link can still
  rotate about the line connecting ?m-2 and ?point.


---
PFT entry: (2,3,coincident)
Case : ?point = ?lf
---

Initial status:
  2-TDOF(?link, ?point, ?plane, ?lf)
  3-RDOF(?link)

Branch variables:
  q_0, denoting a 2-way branch

Plan fragment:
  begin
  translate(?link,
    vec-diff(gmp(?m-1), gmp(?m-2)));
  R[0] = sphere(gmp(?m-2),
    perpendiclar-dist(gmp(?m-2), gmp(?m-1));
  R[1] = insersect(R[0], ?plane, q_0);
  unless circle?(R[1])
    error(perpendiclar-dist(R[0], ?plane), estring[7]);
  R[2] = a-point(R[1]);
  if equal?(?point, ?lf)
    then 3r/p-p(?link, gmp(?m-2), ?point, R[2])
    else 3r/p-p(?link, gmp(?m-2), R[2], ?point);
  R[3] = perpendiclar-base(gmp(?m-2), ?plane);
  R[4] = vec-diff(R[3], gmp(?m-2));
  R[5] = vec-diff(R[2], gmp(?m-2));
  end;

New status:
  0-TDOF(?link, ?point)
  2-RDOF(?link, R[4], R[5])

Explanation:
  Geom ?link is translated to bring ?m-2 into
  coincidence with ?m-1.
  Then, ?link is rotated to bring ?point back onto ?plane.
  After doing this, ?link will have two rotational degrees of freedom.



---
PFT entry: (2,3,coincident)
Case : ?plane = ?lf
---

Initial status:
  2-TDOF(?link, ?point, ?plane, ?lf)
  3-RDOF(?link)

Branch variables:
  q_0, denoting a 2-way branch

Plan fragment:
  begin
  R[0] = vec-diff(gmp(?m-1), gmp(?m-2));
  translate(?link, R[0]);
  R[1] = normal(?plane);
  R[2] = plane(?point, R[1]);
  R[3] = perpendiclar-dist(gmp(?m-2), ?point);
  R[4] = sphere(gmp(?m-2), R[3]);
  R[5] = insersect(R[2], R[4], q_0);
  unless circle?(R[5])
    error(perpendiclar-dist(R[2], R[4]), estring[7]);
  R[6] = a-point(R[5]);
  if equal?(?point, ?lf)
    then 3r/p-p(?link, gmp(?m-2), R[5], ?point);
    else 3r/p-p(?link, gmp(?m-2), ?point, R[5])
  R[7] = perpendiclar-base(gmp(?m-2), ?plane);
  R[8] = vec-diff(R[7], gmp(?m-2));
  R[9] = vec-diff(R[6], gmp(?m-2));
  end;

New status:
  0-TDOF(?link, ?point)
  2-RDOF(?link, R[8], R[9])

Explanation:
  Geom ?link is translated to bring ?m-2 into
  coincidence with ?m-1.
  Then, ?link is rotated to bring ?plane back onto ?point.
  After doing this, ?link will have two rotational degrees of freedom.


"
