package monitor.math

case class Rectangle(min: Point, max: Point) {

  def this(minX: Double, minY: Double, maxX: Double, maxY: Double) = {
    this(new Point(minX, minY), new Point(maxX, maxY))
  }

  def contains(p: Point): Boolean = {
    p.x >= min.x &&
      p.x <= max.x &&
      p.y >= min.y &&
      p.y <= max.y
  }

  def union(p: Point): Rectangle = Rectangle.union(this, p)

  def union(bounds: Rectangle): Rectangle = {
    this.union(bounds.min)
      .union(bounds.max)
  }
}

object Rectangle {
  val ZERO: Rectangle = Rectangle(Point.ZERO, Point.ZERO)

  def union(rect: Rectangle, p: Point): Rectangle = {
    val nMin = (p.x < rect.min.x, p.y < rect.min.y) match {
      case (true, true) => Point(p.x, p.y)
      case (true, false) => Point(p.x, rect.min.y)
      case (false, true) => Point(rect.min.x, p.y)
      case (false, false) => rect.min
    }
    val nMax = (p.x > rect.max.x, p.y > rect.max.y) match {
      case (true, true) => Point(p.x, p.y)
      case (true, false) => Point(p.x, rect.max.y)
      case (false, true) => Point(rect.max.x, p.y)
      case (false, false) => rect.max
    }
    if (nMin == rect.min && nMax == rect.max) rect
    else Rectangle(nMin, nMax)
  }
}

