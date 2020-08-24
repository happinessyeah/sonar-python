class A: ...
class B: ...
class ComparableA:
  def __eq__(self, other): ...
class ComparableB:
  def __ne__(self, other): ...

def custom(x: A, y: B):
  if x == y: ... # Noncompliant {{Fix this equality check; Previous type checks suggest that operands have incompatible types.}}

def custom_comparable(x: A, y: ComparableA, z: ComparableB):
  if x == y: ...
  if x == x: ...
  if x == z: ...

def builtins(x: int, y: str):
  if x == y: ... # Noncompliant
  if x == 'foo': ... # Noncompliant
  if 42 == y: ... # Noncompliant
  if x == None: ... # OK
  if None == y: ... # OK