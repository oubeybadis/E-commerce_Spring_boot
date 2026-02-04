// Global function to toggle dropdowns
function toggleDropdown(dropdownId) {
    console.log('toggleDropdown called for:', dropdownId);
    const dropdown = document.getElementById(dropdownId);
    
    if (dropdown) {
        dropdown.classList.toggle('hidden');
        console.log('Dropdown now:', dropdown.classList.contains('hidden') ? 'HIDDEN' : 'VISIBLE');
        
        // Find the button and rotate its arrow
        const button = document.querySelector(`[aria-controls="${dropdownId}"]`);
        if (button) {
            const arrow = button.querySelector('.arrow-icon');
            if (arrow) {
                arrow.classList.toggle('rotate-180');
            }
        }
    } else {
        console.error('Dropdown element not found:', dropdownId);
    }
}

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

// Close sidebar when a link is clicked
document.addEventListener('DOMContentLoaded', function() {
    const sidebarToggle = document.getElementById('sidebar-toggle');
    const sidebarLinks = document.querySelectorAll('#logo-sidebar a');
    
    // Close sidebar when clicking a link
    sidebarLinks.forEach(link => {
        link.addEventListener('click', function() {
            if (sidebarToggle) {
                sidebarToggle.checked = false;
            }
        });
    });
});

// Dropdown menu toggle for user menu
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

