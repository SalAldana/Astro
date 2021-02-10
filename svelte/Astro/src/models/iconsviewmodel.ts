import { faLinkedin, faGithubAlt, IconDefinition } from '@fortawesome/free-brands-svg-icons'
import { faArrowRight, faGlobe, faMountain, faRobot, faScroll, faWind } from '@fortawesome/free-solid-svg-icons'

export interface iconObject {
    icon: IconDefinition;
    name: string;
    description: string;
    url: string;
    arrow?: IconDefinition;
}

class iconViewModel {

    techStack(): iconObject[] {
        return [
            this.stackSnowpack(),
            this.stackTypescript(),
            this.stackSvelte(),
            this.stackTailwind()
        ];
    }

    stackSnowpack(): iconObject {
        const icon = faMountain;
        const name = "Snowpack";
        const desc = "A modern, lightweight and extremely fast build toolchain for web development.";
        const url = "https://www.snowpack.dev/";
        return {
            icon: icon,
            name: name,
            description: desc,
            url: url,
            arrow: faArrowRight
        };
    }

    stackTypescript(): iconObject {
        const icon = faScroll;
        const name = "TypeScript";
        const desc = "A superset of JavaScript that brings in more object oriented types of features.";
        const url = "https://www.typescriptlang.org/";
        return {
            icon: icon,
            name: name,
            description: desc,
            url: url,
            arrow: faArrowRight
        };
    }

    stackSvelte(): iconObject {
        const icon = faRobot;
        const name = "Svelte";
        const desc = "A modern JavaScript framework that removes all that messy DOM stuff and making development much faster";
        const url = "https://svelte.dev/"
        return {
            icon: icon,
            name: name,
            description: desc,
            url: url,
            arrow: faArrowRight
        };
    }

    stackTailwind(): iconObject {
        const icon = faWind;
        const name = "TailwindCSS"
        const desc = "A CSS framework that gives all the tools needed for creating stunning interfaces quickly";
        const url = "https://tailwindcss.com/"
        return {
            icon: icon,
            name: name,
            description: desc,
            url: url,
            arrow: faArrowRight
        };
    }

    socialStack(): iconObject[] {
        return [
            this.socialGitHub(),
            this.socialLinkedIn(),
            this.socialWeb()
        ];
    }

    socialGitHub(): iconObject {
        const icon = faGithubAlt;
        const name = "Github";
        const url = "https://github.com/SalAldana/Astro";
        return { icon: icon, name: name, description: "", url: url };
    }

    socialLinkedIn(): iconObject {
        const icon = faLinkedin;
        const name = "LinkedIn";
        const url = "https://www.linkedin.com/company/level-3-studios";
        return { icon: icon, name: name, description: "", url: url };
    }

    socialWeb(): iconObject {
        const icon = faGlobe;
        const name = "Level 3 Studios, LLC"
        const url = "https://www.level3studios.net"
        return { icon: icon, name: name, description: "", url: url };
    }
}

export { iconViewModel }