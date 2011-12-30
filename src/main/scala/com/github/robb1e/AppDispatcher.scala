package com.github.robb1e

import org.scalatra._
import javax.servlet._
import javax.servlet.http._

class AppServlet extends ScalatraServlet {

  def status: Int = {
    val getter = response.getClass.getDeclaredMethod("getStatus")
    getter.invoke(response).asInstanceOf[Int]
  }

  get("/") {
    <ul>
      <li><a href="exception">Throw exception</a></li>
      <li><a href="halt">Call halt</a></li>
      <li><a href="status">Call status</a></li>
    </ul>
  }

  get("/exception") {
    throw new RuntimeException("boom!")
  }

  get("/halt") {
    halt(status = 500, reason = "boom town")
  }

  get("/status") {
    status(500)
  }

  after() {
    println("status = " + status)
  }
}

class AppFilter extends AbstractHttpFilter {

  def doHttpFilter(request: HttpServletRequest, resp: HttpServletResponse, chain: FilterChain) {
    chain.doFilter(request, resp)
  }

}

abstract class AbstractHttpFilter extends Filter {
  def init(filterConfig: FilterConfig) {}
  def destroy() {}

  final def doFilter(req: ServletRequest, resp: ServletResponse, chain: FilterChain) {
    (req, resp) match {
      case (httpReq: HttpServletRequest, httpResp: HttpServletResponse) => {
        println("**** >>>> Is a HttpServletRequest/Response")
        doHttpFilter(httpReq, httpResp, chain)
      }
      case _ => {
        println("**** >>>> Is not a HttpServletRequest/Response")
        chain.doFilter(req, resp)
      }
    }
  }

  def doHttpFilter(servletRequest: HttpServletRequest, resp: HttpServletResponse, chain: FilterChain)

}
