package xyz.miyayu.yobsub.yobsubcord.pubsub

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import xyz.miyayu.yobsub.yobsubcord.EnvWrapper

@RestController
class Challenge {

    val logger = LoggerFactory.getLogger(Challenge::class.java)
    val weblogger = LoggerFactory.getLogger("xyz.miyayu.yobsub.netlog")
    @GetMapping("hub")
    fun getChallenge(
        @RequestParam params:Map<String,String>,
        @RequestParam(name = "hub_mode", required = true)hub_mode: String,
        @RequestParam(name = "hub_challenge", required = true)hub_challenge:String,
        @RequestParam(name = "hub_verify_token", required = true)hub_verify_token:String
    ) : ResponseEntity<String> {
        weblogger.info("--Challenge Start--")
        params.forEach{
            weblogger.info(it.key + ":" + it.value)
        }
        return if(hub_mode == "subscribe" || hub_mode == "unsubscribe"){
            if(hub_verify_token == EnvWrapper.TOKEN){
                weblogger.info("--OK--")
                ResponseEntity<String>(hub_challenge,HttpStatus.OK)
            }else{
                weblogger.info("--ERROR--")
                ResponseEntity<String>("token unmatch", HttpStatus.BAD_REQUEST)
            }
        }else{
            weblogger.info("--ERROR--")
            ResponseEntity<String>("HTTP/1.1 404 Not Found", HttpStatus.BAD_REQUEST)
        }
    }
}