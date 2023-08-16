"use client"

import React, { useEffect, useState } from "react"
import Image from "next/image"
import heroSvg from '@/assets/hero.svg'
import AOS from 'aos';
import 'aos/dist/aos.css';



export default function ExpInt() {

    useEffect(()=> {
        AOS.init();

    }, [])

    return (
        <section id="expInt" className=" h-[100vh] sm:pt-2 bg-black sm:pb-16 lg:pb-20 xl:pb-12 bg-[url('/about-edit.svg')] bg-center bg-cover bg-no-repeat " data-aos = "fade-in" data-aos-delay="500" data-aos-duration="1700">
            
                <h1 className="flex mt-[12rem] mb-[4rem] max-w-fit justify-center mx-auto text-slate-100 text-[3.2rem] animate-pulse">Express Interest</h1>

                <div className="flex flex-row mt-[3rem] justify-center space-x-8 mb-[4rem]">
                    <div className=" basis-1/2 flex-col text-purple-200 text-2xl max-w-fit space-y-[0.25rem]  ">
                        <label className="flex  text-[1.3rem] text-purple-100/90    ">Name  </label> <br />
                        <label className=" flex  text-[1.3rem] text-purple-100/90 ">Email  </label><br />
                        <label className="flex  text-[1.3rem] text-purple-100/90  ">How you came to know about us?  </label>
                    </div>

                    <div className="basis-1/2 flex-col text-purple-200  max-w-fit space-y-2">
                
                        <input placeholder="Full name" className="flex text-[1.3rem] bg-purple-100/30 placeholder-purple-100/40 text-purple-100/70 px-[0.5rem] border-2 border-purple-200/10 rounded-md outline-none active:outline-none focus:outline-none focus:border-purple-400 focus:border-2 max-w-[25rem] w-[20rem]"></input><br />
                        <input placeholder="Your email id" className="flex text-[1.3rem] bg-purple-100/30 placeholder-purple-100/40 text-purple-100/70 px-[0.5rem] border-2 border-purple-200/10 rounded-md outline-none active:outline-none focus:outline-none focus:border-purple-400 focus:border-2 max-w-[25rem] w-[20rem]"></input><br />
                        <input placeholder="Eg: Social Media, Friends, etc." className="flex text-[1.3rem] bg-purple-100/30 placeholder-purple-100/40 text-purple-100/70 px-[0.5rem] border-2 border-purple-200/10 rounded-md outline-none active:outline-none focus:outline-none focus:border-purple-400 focus:border-2 max-w-[25rem] w-[20rem]"></input>
                    </div>
                </div>

                <button className="flex max-w-fit mx-auto items-center justify-center px-6 py-1 font-normal text-white/80 bg-purple-950 border border-transparent rounded-[0.5rem] mt-[2.4rem] text-[1.3rem]"
                        >
                            {" "}
                            Submit{" "}
                </button>
            
        </section>
    )
}

