(function () {
    const reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;

    if (!reduceMotion && window.Lenis) {
        const lenis = new Lenis({
            duration: 1.1,
            smoothWheel: true,
            wheelMultiplier: 0.9
        });

        function raf(time) {
            lenis.raf(time);
            window.requestAnimationFrame(raf);
        }
        window.requestAnimationFrame(raf);
    }

    if (!reduceMotion && window.gsap) {
        gsap.utils.toArray(".reveal-block").forEach((element) => {
            gsap.from(element, {
                opacity: 0,
                y: 18,
                duration: 0.65,
                ease: "power3.out",
                scrollTrigger: false
            });
        });
    }
})();
