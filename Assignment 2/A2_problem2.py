# -*- coding: utf-8 -*-
"""
Assignment 2 Problem 2

## Part 2 (a)
"""

from collections.abc import Iterable

def flatten(l):
    for el in l:
        if isinstance(el, Iterable) and not isinstance(el, (str, bytes)):
            yield from flatten(el)
        else:
            yield el

inlist = [[[2],4],[[[[[6],8]]]],[[9],[[10]]]]
for x in flatten(inlist):
  if x%2 != 0:
    break
  print(x)

"""
## Part 2 (b)
"""

def flatten2(l, thnk):
    for el in l:
        if isinstance(el, Iterable) and not isinstance(el, (str, bytes)):
            flatten2(el, thnk)
        else:
            thunk(el)

def thunk(x):
  if x%2 != 0:
    raise Exception
  print(x)

inlist = [[[2],4],[[[[[6],8]]]],[[9],[[10]]]]
try:
  flatten2(inlist, thunk)
except Exception:
  pass
