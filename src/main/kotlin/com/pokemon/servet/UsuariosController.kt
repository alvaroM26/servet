package com.pokemon.servet

import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UsuariosController (private  val usuarioRepository: UsuarioRepository) {

    @PostMapping("registro")
    @Synchronized
    fun reistroUsuario(@RequestBody usuario: Usuario): Any {

        //COMPROBAR SI EL USUARIO EXISTE
        val userOpcional = usuarioRepository.findById(usuario.nombre)

        //SI EL USUARIO EXISTE PASAR A COMPROBAR LA CONTRASEÑA
        return if (userOpcional.isPresent) {

            //OBTENEMOS EL USUARIO ENCONTRADO
            val user = userOpcional.get()

            //COMPROBAMOS LA CONTRASEÑA
            if (user.pass == usuario.pass){
                user
            }else{
                "Contraseña incorrecta"
            }

        } else {
            //SI NO EXISTE EL USUARIO CREARLO Y MSOTRARLO
            usuarioRepository.save(usuario)
            usuario
        }

    }

}