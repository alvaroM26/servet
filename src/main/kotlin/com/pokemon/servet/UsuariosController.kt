package com.pokemon.servet

import org.springframework.web.bind.annotation.GetMapping
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
            if (user.pass == usuario.pass) {
                user
            } else {
                "Contraseña incorrecta"
            }

        } else {
            //SI NO EXISTE EL USUARIO CREARLO Y MSOTRARLO
            usuarioRepository.save(usuario)
            usuario
        }

    }

    @PostMapping("pokemonFavorito/{token}/{pokemonId}")
    fun requestPokemonFavorito(@PathVariable token: String, @PathVariable pokemonId: Int): String {

        usuarioRepository.findAll().forEach { user ->

            if (user.token == token) {
                user.pokemonFavoritoId = pokemonId
                usuarioRepository.save(user)
                return "El usuario ${user.nombre} tiene un nuevo Pokemon favorito"
            }

        }

        return "TOKEN NO ENCONTRADO"
    }

    @GetMapping("mostrarPokemonFavorito/{token}")
    fun requestmostrarPokemonFavorito(@PathVariable token: String): Any {

        usuarioRepository.findAll().forEach { user ->
            if (user.token == token) {

                listaPokemon.listaPokemon.forEach {

                    if (user.pokemonFavoritoId == it.id) {
                        return it
                    }

                }
                return "El usuario no tiene pokemon favorito"

            }
        }
        return "Token no encontrado"
    }

    @PostMapping("PokemonCapturados/{token}/{pokemonId}")
    fun requestPokemonCapturados(@PathVariable token: String, @PathVariable pokemonId: Int): String {

        usuarioRepository.findAll().forEach { user ->

            if (user.token == token) {
                user.pokemonsCapturado.add(pokemonId)
                usuarioRepository.save(user)
                return "El usuario ${user.nombre} tiene ${user.pokemonsCapturado} capturados"
            }

        }

        return "Token no encontrado"

    }

    @GetMapping("mostrarPokemonCapturados/{token}")
    fun requestmostrarPokemonCapturados(@PathVariable token: String): Any {

        val listacapturadosinfo = mutableListOf<String>()

        usuarioRepository.findAll().forEach { user ->

            if (user.token == token) {

                user.pokemonsCapturado.forEach {

                    listaPokemon.listaPokemon.forEach { encon ->

                        if (encon.id == it) {
                            listacapturadosinfo.add(encon.name)
                        }

                    }

                }
                return if (listacapturadosinfo.isEmpty())
                    "El usuario no tiene pokemons capturados"
                else
                    listacapturadosinfo

            }
        }

        return "Token no encontrado"

    }

    @PostMapping("intercambiarPokemon/{tokenUsuario1}/{tokenUsuario2}/{pokemonId1}/{pokemonId2}")
    fun requestintercambiarPokemon(@PathVariable tokenUsuario1: String,@PathVariable tokenUsuario2: String, @PathVariable pokemonId1: Int, @PathVariable pokemonId2: Int) :Any {

        usuarioRepository.findAll().forEach {user1->
            if (user1.token == tokenUsuario1){

                usuarioRepository.findAll().forEach { user2->
                    if (user2.token == tokenUsuario2){

                        user1.pokemonsCapturado.forEach {id1->
                            if (id1 == pokemonId1){

                                user2.pokemonsCapturado.forEach { id2->
                                    if (id2 == pokemonId2){

                                        user1.pokemonsCapturado.remove(id1)
                                        user1.pokemonsCapturado.add(id2)
                                        usuarioRepository.save(user1)

                                        user2.pokemonsCapturado.remove(id2)
                                        user2.pokemonsCapturado.add(id1)
                                        usuarioRepository.save(user2)
                                        return "Intercambio realizado con exito"
                                    }

                                }
                                return "La persona 2 no tiene el id2 del pokemon"

                            }

                        }
                        return "La persona 1 no tiene el id1 del pokemon"
                    }

                }
                return "El token2 no existe"

            }
        }
        return "El token1 no existe"

    }
}