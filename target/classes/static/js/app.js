// Theme toggle functionality
var themeToggleDarkIcon = document.getElementById('theme-toggle-dark-icon');
var themeToggleLightIcon = document.getElementById('theme-toggle-light-icon');

// Change the icons inside the button based on previous settings
if (localStorage.getItem('color-theme') === 'dark' || (!('color-theme' in localStorage) && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
    if (themeToggleLightIcon) themeToggleLightIcon.classList.remove('hidden');
} else {
    if (themeToggleDarkIcon) themeToggleDarkIcon.classList.remove('hidden');
}

var themeToggleBtn = document.getElementById('theme-toggle');

if (themeToggleBtn) {
    themeToggleBtn.addEventListener('click', function() {
        // toggle icons inside button
        if (themeToggleDarkIcon) themeToggleDarkIcon.classList.toggle('hidden');
        if (themeToggleLightIcon) themeToggleLightIcon.classList.toggle('hidden');

        // if set via local storage previously
        if (localStorage.getItem('color-theme')) {
            if (localStorage.getItem('color-theme') === 'light') {
                document.documentElement.classList.add('dark');
                localStorage.setItem('color-theme', 'dark');
            } else {
                document.documentElement.classList.remove('dark');
                localStorage.setItem('color-theme', 'light');
            }
        // if NOT set via local storage previously
        } else {
            if (document.documentElement.classList.contains('dark')) {
                document.documentElement.classList.remove('dark');
                localStorage.setItem('color-theme', 'light');
            } else {
                document.documentElement.classList.add('dark');
                localStorage.setItem('color-theme', 'dark');
            }
        }
    });
}

// Sidebar drawer toggle functionality
document.addEventListener('DOMContentLoaded', function() {
    const drawerToggle = document.querySelector('[data-drawer-toggle="logo-sidebar"]');
    const drawer = document.getElementById('logo-sidebar');
    const dropdownToggles = document.querySelectorAll('[data-collapse-toggle]');
    
    // Handle sidebar toggle
    if (drawerToggle && drawer) {
        drawerToggle.addEventListener('click', function() {
            drawer.classList.toggle('-translate-x-full');
        });
    }
    
    // Handle dropdown toggles in sidebar
    dropdownToggles.forEach(toggle => {
        toggle.addEventListener('click', function() {
            const targetId = this.getAttribute('aria-controls');
            const target = document.getElementById(targetId);
            if (target) {
                target.classList.toggle('hidden');
            }
        });
    });
    
    // Close sidebar when clicking outside (on mobile)
    document.addEventListener('click', function(event) {
        if (window.innerWidth < 1024 && drawer && !drawer.contains(event.target) && !drawerToggle.contains(event.target)) {
            if (!drawer.classList.contains('-translate-x-full')) {
                drawer.classList.add('-translate-x-full');
            }
        }
    });
});

// Dropdown menu toggle
document.addEventListener('DOMContentLoaded', function() {
    const dropdownToggle = document.querySelector('[data-dropdown-toggle="dropdown-user"]');
    const dropdownMenu = document.getElementById('dropdown-user');
    
    if (dropdownToggle && dropdownMenu) {
        dropdownToggle.addEventListener('click', function(e) {
            e.stopPropagation();
            dropdownMenu.classList.toggle('hidden');
        });
        
        // Close dropdown when clicking outside
        document.addEventListener('click', function(e) {
            if (!dropdownToggle.contains(e.target) && !dropdownMenu.contains(e.target)) {
                dropdownMenu.classList.add('hidden');
            }
        });
    }
});

