package com.pokemon.servet

import  org.springframework.data.repository.findByIdOrNull
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
    fun requestRegistroUsuario(@RequestBody usuario: Usuario): Any {

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

    @PostMapping("pokemonFavorito/{token}/{pokemonId}")
    fun requestPokemonFavorito(@PathVariable token : String, @PathVariable pokemonId : Int) : String {

        usuarioRepository.findAll().forEach { user ->

            if (user.token == token){
                user.pokemonFavoritoId = pokemonId
                usuarioRepository.save(user)
                return "El usuario ${user.nombre} tiene un nuevo Pokemon favorito"
            }

        }

        return "TOKEN NO ENCONTRADO"
    }

    @GetMapping("mostrarPokemonFavorito/{token}")
    fun requestmostrarPokemonFavorito(@PathVariable token: String) : Any {

        usuarioRepository.findAll().forEach {user ->
            if (user.token == token){

                listaPokemon.listaPokemon.forEach {

                    if (user.pokemonFavoritoId == it.id  ){
                        return it
                    }

                }
                return "El usuario no tiene pokemon favorito"

            }
        }
        return "Token no encontrado"
    }

    @GetMapping("mostrarPokemonCapturados/{token}")
    fun requestmostrarPokemonCapturados(@PathVariable token: String) : Any {


    }

}