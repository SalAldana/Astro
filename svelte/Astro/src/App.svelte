<script>
  import { iconViewModel } from "./models/iconsviewmodel";
  import { astroViewModel } from "./models/astroviewmodel";
  import Fa from "svelte-fa";
  import Select from "svelte-select";
  import DatePicker from "svelte-touch-datepicker";

  const iconModel = new iconViewModel();
  const astroModel = new astroViewModel();

  let gradientStart = astroModel.getGradientStart();
  let gradientEnd = astroModel.getGradientStop();
  let primaryColor = astroModel.getPrimaryColor();
  let primaryHoverColor = astroModel.getPrimaryHoverColor();
  let secondaryColor = astroModel.getSecondaryColor();
  let secondaryHoverColor = astroModel.getSecondaryHoverColor();
  let textColor = astroModel.getTextColor();
  let cityImage = astroModel.getCityImage();

  let phaseDisplayText = astroModel.getPhaseTitle();
  let sunSubtitleText = astroModel.getSunPhaseSubtitle();
  let moonSubtitleText = astroModel.getMoonPhaseSubtitle();
  let randomLocations = astroModel.getRandomLocations();
  let boundDate = astroModel.viewDate;

  const phaseItems = ["SUNRISE", "NOON", "SUNSET", "NIGHT"];
  let selectedPhase = "NOON";
  function handlePhaseSelect(event) {
    astroModel.setNewPhase(event.detail.value);
    updateUI();
  }

  let selectedLocation = "San Francisco";
  function handleLocationSelect(event) {
    astroModel.setNewLocation(event.detail.value);
    updateUI();
  }

  function handleDateSelect(event) {
    astroModel.setNewDate(event.detail.date);
    updateUI();
  }

  function updateUI() {
    gradientStart = astroModel.getGradientStart();
    gradientEnd = astroModel.getGradientStop();
    primaryColor = astroModel.getPrimaryColor();
    primaryHoverColor = astroModel.getPrimaryHoverColor();
    secondaryColor = astroModel.getSecondaryColor();
    secondaryHoverColor = astroModel.getSecondaryHoverColor();
    textColor = astroModel.getTextColor();
    cityImage = astroModel.getCityImage();
    phaseDisplayText = astroModel.getPhaseTitle();
    sunSubtitleText = astroModel.getSunPhaseSubtitle();
    moonSubtitleText = astroModel.getMoonPhaseSubtitle();
    randomLocations = astroModel.getRandomLocations();
    boundDate = astroModel.viewDate;
  }
</script>

<!-- Header Section -->
<nav class="bg-gray-800">
  <div class="max-w-7xl mx-auto px-2 sm:px-6 lg:px-8">
    <div class="relative flex items-center justify-between h-16">
      <div
        class="flex-1 flex items-center justify-center sm:items-stretch
        sm:justify-start"
      >
        <div class="flex-shrink-0 flex items-center">
          <img
            class="lg:block h-8 w-auto"
            src="./assets/astro_logo.svg"
            alt="Astro"
          />
        </div>
      </div>

      <div
        class="absolute inset-y-0 right-0 flex items-center pr-2 sm:static
        sm:inset-auto sm:ml-6 sm:pr-0"
      >
        {#each iconModel.socialStack() as stack}
          <a
            href={stack.url}
            target="_blank"
            class="bg-gray-800 p-1 rounded-full text-gray-400 hover:text-white
            focus:outline-none focus:ring-2 focus:ring-offset-2
            focus:ring-offset-gray-800 focus:ring-white"
          >
            <span class="sr-only">{stack.name}</span>
            <Fa icon={stack.icon} />
          </a>
        {/each}
      </div>
    </div>
  </div>
</nav>

<!-- Hero Section -->
<div
  class="relative bg-gradient-to-b {gradientStart}
  {gradientEnd} overflow-hidden"
>
  <div class="max-w-7xl mx-auto">
    <div
      class="relative z-10 pb-8 bg-white sm:pb-16 md:pb-20 lg:max-w-2xl
      lg:w-full lg:pb-28 xl:pb-32"
    >
      <svg
        class="hidden lg:block absolute right-0 inset-y-0 h-full w-48 text-white
        transform translate-x-1/2"
        fill="currentColor"
        viewBox="0 0 100 100"
        preserveAspectRatio="none"
        aria-hidden="true"
      >
        <polygon points="50,0 100,0 50,100 0,100" />
      </svg>

      <div class="relative pt-6 px-4 sm:px-6 lg:px-8">
        <nav
          class="relative flex items-center justify-between sm:h-10
          lg:justify-start"
          aria-label="Global"
        />
      </div>

      <main
        class=" relative mt-10 mx-auto max-w-7xl px-4 sm:mt-12 sm:px-6 md:mt-16
        lg:mt-20 lg:px-8 xl:mt-28"
      >
        <div class="sm:text-center lg:text-left">
          <h1
            class="text-4xl tracking-tight font-extrabold text-gray-900
            sm:text-5xl md:text-6xl"
          >
            <span class="block xl:inline">{phaseDisplayText}</span>
            <span class="block {textColor} xl:inline">{sunSubtitleText}</span>
          </h1>
          <p
            class="mt-3 text-base text-gray-500 sm:mt-5 sm:text-lg sm:max-w-xl
            sm:mx-auto md:mt-5 md:text-xl lg:mx-0"
          >
            {moonSubtitleText}
          </p>
          <div class="mt-5 sm:mt-8 sm:flex sm:justify-center lg:justify-start">
            <div
              class=" flex items-center justify-center px-4 py-3
               border-0 text-base font-medium rounded-md {primaryColor}
                hover:{primaryHoverColor} md:py-4 md:text-lg md:px-10"
            >
              <Select
                items={phaseItems}
                selectedValue={selectedPhase}
                isClearable={false}
                isSearchable={true}
                on:select={handlePhaseSelect}
              />
            </div>
            <div
              class="mt-3 sm:mt-0 sm:ml-3 w-full flex items-center justify-center px-8 py-3 border
                border-transparent text-base font-medium rounded-md {textColor}
                {secondaryColor} hover:{secondaryHoverColor} md:py-4 md:text-lg
                md:px-10"
            >
              <Select
                items={randomLocations}
                selectedValue={selectedLocation}
                isClearable={false}
                isSearchable={true}
                on:select={handleLocationSelect}
              />
            </div>
            <div
              class="mt-3 sm:mt-0 sm:ml-3 w-full flex items-center justify-center px-8 py-3 border
                border-transparent text-base font-medium rounded-md {textColor}
                {secondaryColor} hover:{secondaryHoverColor} md:py-4 md:text-lg
                md:px-10"
            >
              <DatePicker date={boundDate} on:confirmDate={handleDateSelect} />
            </div>
          </div>
        </div>
      </main>
    </div>
  </div>
  <div class="lg:absolute lg:inset-y-0 lg:right-0 lg:w-1/2">
    <img
      class="h-56 w-full object-cover sm:h-72 md:h-96 lg:w-full lg:h-full"
      src="./assets/{cityImage}.svg"
      alt=""
    />
  </div>
  <br />
</div>

<!-- Thanks Section -->
<div class="py-12 bg-gray-50">
  <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
    <div class="lg:text-center">
      <p
        class="mt-2 text-3xl leading-8 font-extrabold tracking-tight
        text-gray-900 sm:text-4xl"
      >
        Built with a modern toolset
      </p>
      <p class="mt-4 max-w-2xl text-xl text-gray-500 lg:mx-auto">
        This app was designed as part of a learning experience combining the
        following frameworks
      </p>
    </div>

    <div class="mt-10">
      <dl
        class="space-y-10 md:space-y-0 md:grid md:grid-cols-2 md:gap-x-8
        md:gap-y-10"
      >
        {#each iconModel.techStack() as stack}
          <div class="flex">
            <div class="flex-shrink-0">
              <div
                class="flex items-center justify-center h-12 w-12 rounded-md {primaryColor}
                text-white"
              >
                <Fa icon={stack.icon} />
              </div>
            </div>
            <div class="ml-4">
              <dt class="text-lg leading-6 font-medium text-gray-900">
                {stack.name}
              </dt>
              <dd class="mt-2 text-base text-gray-500">{stack.description}</dd>
              <a
                href={stack.url}
                target="_blank"
                class="inline-flex items-center {textColor}"
              >
                Learn More
                <Fa class="ml-2" icon={stack.arrow} />
              </a>
            </div>
          </div>
        {/each}
      </dl>
    </div>
  </div>
</div>

<style>
  :global(body) {
    margin: 0;
    font-family: Arial, Helvetica, sans-serif;
  }
</style>
