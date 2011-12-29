package com.github.robb1e

import org.scalatra._
import javax.servlet._
import javax.servlet.http._

class AppServlet extends ScalatraServlet {

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

}

class AppFilter extends AbstractHttpFilter {

  def doHttpFilter(request: HttpServletRequest, resp: HttpServletResponse, chain: FilterChain) {
    val wrappedResponse = new HttpServletResponseWrapper(resp) {
      var statusCode: Int = 200

      override def setStatus(status: Int): Unit = {
        super.setStatus(status)
        statusCode = status
      }

      override def sendError(status: Int, message: String): Unit = {
        super.sendError(status, message)
        statusCode = status
      }

    }

    chain.doFilter(request, wrappedResponse)

    println("**** >>>> Status code is " + wrappedResponse.statusCode + " for " + request.getRequestURI)

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